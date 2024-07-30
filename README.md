# Multithreaded [Gephi](https://gephi.org/) Force Atlas2 Layout in 2 or 3-d

## Features

- Multilevel Layout: Combines the benefits of force-directed algorithms with multilevel techniques, allowing for efficient layout of large graphs.
- Fast Convergence: Utilizes an adaptive cooling scheme to enhance convergence speed, making it suitable for large datasets.
- Force-Directed Model: Applies repulsive and attractive forces between nodes, ensuring a natural and visually appealing layout.
- Complexity Reduction: Reduces computational complexity to O(NlogN)
- O(NlogN) by approximating distant nodes as super-nodes, allowing for efficient processing of large networks.
- Customizable Parameters: Offers adjustable parameters such as optimal distance, step size, and gravity, enabling fine-tuning of the layout for specific graph characteristics.
- Automatic Stopping: The algorithm automatically determines when to stop based on convergence criteria, simplifying user interaction.
- Command line interface 

## Installation
Download gephi-toolkit-0.9.2-all.jar and yifanhu.jar from https://github.com/phlawlessDevelopment/yifanhu/releases


## Command Line Usage

```
java -Djava.awt.headless=true -Xmx8g -cp yifanhu.jar:gephi-toolkit-0.9.2-all.jar Main flags 
```

where flags are

Flag | Description | Default Value
--- | --- | ---
--input | Input graph in one of Gephi input file formats https://gephi.org/users/supported-graph-formats/ |
--output | Output file | 
--iterations | Number of iterations | 50
--optimalDistance | The natural length of the springs. Bigger values mean nodes will be farther apart | 100f
--barnesHutTheta | Theta of the Barnes Hut optimization | 1.2f
--stepRatio | The ratio used to update the step size across iterations | 0.95f 
--initialStep | The initial step size used in the integration phase. Set this value to a meaningful size compared to the optimal distance (10% is a good starting point) | 20.0f
--barnesHutTheta | The theta parameter for Barnes-Hut opening criteria. Smaller values mean more accuracy | 1.2f
--adaptiveCooling | Controls the use of adaptive cooling. It is used help the layout algoritm to avoid energy local minima | true 
--quadTreeMaxLevel | The maximum level to be used in the quadtree representation. Greater values mean more accuracy | 10
--relativeStrength | The relative strength between electrical force (repulsion) and spring force (attraction) | 0.2f
--convergenceThreshold | Relative energy convergence threshold. Smaller values mean more accuracy | 1.0E-4f


## Example Datasets
[Gephi example datasets](https://github.com/gephi/gephi/wiki/Datasets)

