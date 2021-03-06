package wiremap;

public class WiremapOutlinedSphere extends WiremapSphere {
    private int mOutlineThickness;
    private int mOutlineColor;

    public WiremapOutlinedSphere(Wiremap map, float x, float y, float z,
            int baseColor, float radius, int outlineThickness, int outlineColor) {
        super(map, x, y, z, baseColor, radius);
        setOutlineThickness(outlineThickness);
        setOutlineColor(outlineColor);
    }

    public void display() {
        mMap.getParent().pushMatrix();
        displayCenter();
        displayOutline();
        mMap.getParent().popMatrix();
    }

    private void displayOutline() {
        for(int i = 0; i < mMap.getWireCount(); i++) {
            // if a wire's x coord is close enough to the globe's center
            if(mMap.getWireDepth(i) >= 0
                    && (mMap.getWireX(i) >= (mX - mRadius))
                    && (mMap.getWireX(i) <= (mX + mRadius))) {
                // find the distance from the wire to the globe's center
                double localHyp;
                double actualDistance =
                        Math.sqrt(Math.pow(mMap.getWireX(i) - mX, 2)
                                + Math.pow(mMap.getWireZ(i) - mZ, 2));
                if(mMap.getWireZ(i) < mZ) {
                    localHyp =
                            Math.sqrt(Math.pow(mMap.getWireX(i) - mX, 2)
                                    + Math.pow(mMap.getWireZ(i)
                                            + mMap.getDepthUnit() / 2 - mZ, 2));
                } else {
                    localHyp =
                            Math.sqrt(Math.pow(mMap.getWireX(i) - mX, 2)
                                    + Math.pow(mMap.getWireZ(i)
                                            - mMap.getDepthUnit() / 2 - mZ, 2));
                }
                // if the wire's xz coord is close enough to the globe's center
                if(localHyp <= mRadius) {
                    // find the height of the globe at that point
                    double centerY =
                            Math.sqrt(Math.pow(mRadius, 2)
                                    - Math.pow(actualDistance, 2));
                    double yMinProjection =
                            (mY + centerY) * mMap.getDepth() / mMap.getWireZ(i);
                    double yMaxProjection =
                            (mY - centerY) * mMap.getDepth() / mMap.getWireZ(i);

                    WiremapSliver sliver =
                            new WiremapSliver(mMap,
                                    i,
                                    (int) (yMaxProjection * mMap.getPixelsPerInch()),
                                    mOutlineColor,
                                    mOutlineThickness,
                                    0,
                                    0);
                    sliver.display();

                    sliver.setStartingHeight((int) (yMinProjection * mMap.getPixelsPerInch()));
                    sliver.display();
                }
            }
        }
    }

    public void setOutlineThickness(int thickness) {
        mOutlineThickness = thickness;
    }

    public void setOutlineColor(int outlineColor) {
        mOutlineColor = outlineColor;
    }
}
