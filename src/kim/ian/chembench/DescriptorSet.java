package kim.ian.chembench;

import java.util.List;
import java.util.Map;

public class DescriptorSet {
    private DescriptorType descriptorType;
    private Map<String, List<Double>> matrix;
    private List<String> descriptorNames;

    public DescriptorType getDescriptorType() {
        return descriptorType;
    }

    public void setDescriptorType(DescriptorType descriptorType) {
        this.descriptorType = descriptorType;
    }

    public Map<String, List<Double>> getMatrix() {
        return matrix;
    }

    public void setMatrix(Map<String, List<Double>> matrix) {
        this.matrix = matrix;
    }

    public List<String> getDescriptorNames() {
        return descriptorNames;
    }

    public void setDescriptorNames(List<String> descriptorNames) {
        this.descriptorNames = descriptorNames;
    }
}
