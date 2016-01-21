# hybrid-descriptors
**Assignment**: combine two descriptor set files of any type into a new, valid `.x` file containing the descriptors of both sets.

### Requirements
- Output file is a valid `.x` file
    - The first line representing the shape of the matrix must be correct
    - Fields should be delimited by the tab character (`\t`)
- Descriptor names in the new `.x` file are prefixed by `DescriptorSet.descriptorType` and an underscore (`_`)
    - For example, a descriptor named `ALogP` in the CDK descriptor set should be renamed to `CDK_ALogP` in the hybrid `.x` file

### Provided resources
- The `ReadDescriptors#read` method will take a Java `Path` and return a `DescriptorSet` object for any descriptor set type
- The `examples/` directory contains the descriptor set files for the `Anticonvulsants 48` dataset