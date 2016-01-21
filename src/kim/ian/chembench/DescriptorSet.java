package kim.ian.chembench;

import java.util.Map;

public class DescriptorSet {
    private DescriptorType descriptorType;
    private Map<String, Map<String, Double>> matrix;

    public DescriptorType getDescriptorType() {
        return descriptorType;
    }

    public void setDescriptorType(DescriptorType descriptorType) {
        this.descriptorType = descriptorType;
    }

    public Map<String, Map<String, Double>> getMatrix() {
        return matrix;
    }

    public void setMatrix(Map<String, Map<String, Double>> matrix) {
        this.matrix = matrix;
    }
}
