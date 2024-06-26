package util;

public class SigmoidScaler {

    private float k; // steepness factor
    private float midpoint;
    private float supremum;

    public SigmoidScaler(float min, float max, float k, float supremum) {
        this.midpoint = (float) ((min + max) / 2.0);
        this.k = k;
        this.supremum = supremum;
    }

    /**
     * Scales the input value to between 0 and 1 using the sigmoid function.
     * @param x
     * @return
     */
    public float scale(float x) {
        return (float) ((float) supremum / (1.0 + Math.exp(-k * (x - midpoint))));
    }

    public static void main(String[] args) {
        float min = 1.0f;
        float max = 100.0f;
        float k = 0.1f;  // Adjust this parameter based on desired sensitivity

        SigmoidScaler scaler = new SigmoidScaler(min, max, k, 1.0f);
        for (float x = min; x <= max; x += 1.0f) {
            System.out.println(x + ": " + scaler.scale(x));
        }
    }
}
