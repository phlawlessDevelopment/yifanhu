
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import java.io.*;
import java.util.*;

import yifanHu.YifanHuLayout;

public class Main {

    private static Map<String, Arg> argsMap = new LinkedHashMap<>();

    private static void writeOutput(Graph g, boolean is3d, Set<String> formats, String output) {
        try {
            // ExporterCSV, ExporterDL, ExporterGDF, ExporterGEXF, ExporterGML,
            // ExporterGraphML, ExporterPajek, ExporterVNA, PDFExporter, PNGExporter,
            // SVGExporter
            ExportController ec = Lookup.getDefault().lookup(ExportController.class);
            for (String format : formats) {
                if (format.equals("txt")) {
                    PrintWriter pw = new PrintWriter(
                            new FileWriter(output + (output.toLowerCase().endsWith("." + format) ? "" : "." + format)));
                    pw.print("id\tx\ty" + (is3d ? "\tz" : "") + "\n");
                    for (Node n : g.getNodes()) {
                        pw.print(n.getId());
                        pw.print("\t");
                        pw.print(n.x());
                        pw.print("\t");
                        pw.print(n.y());
                        if (is3d) {
                            pw.print("\t");
                            pw.print(n.z());
                        }
                        pw.print("\n");
                    }
                    pw.close();
                } else {
                    ec.exportFile(new File(output + (output.toLowerCase().endsWith("." + format) ? "" : "." + format)),
                            ec.getExporter(format));
                }
            }
        } catch (IOException x) {
            x.printStackTrace();
            System.exit(1);
        }
    }

    private static void addArg(String flag, String description, boolean not_boolean, Object defaultValue) {
        argsMap.put("--" + flag.toLowerCase(), new Arg(flag, description, not_boolean, "" + defaultValue));
    }

    private static void addArg(String flag, String description, boolean not_boolean) {
        argsMap.put("--" + flag.toLowerCase(), new Arg(flag, description, not_boolean, null));
    }

    private static String getArg(String flag) {
        Arg a = argsMap.get("--" + flag.toLowerCase());
        return a != null ? a.value : null;
    }

    public static void main(String[] args) throws IOException {

        addArg("input",
                "Input graph in one of Gephi input file formats https://gephi.org/users/supported-graph-formats/",
                true);
        addArg("output", "Output file", true);
        addArg("iterations", "Number of iterations. Mutually exclusive with --targetChangePerNode", true);
        addArg("optDist", "optimal distance between nodes", true);
        addArg("step", "", true);
        addArg("stepRatio", "", true);
        addArg("initialStep", "", true);
        addArg("converged", "", true);
        addArg("barnesHutTheta", "", true);
        addArg("adaptiveCooling", "", true);
        addArg("quadTreeMaxLevel", "", true);
        addArg("relativeStrength", "", true);
        addArg("convergenceThreshold", "", true);

        for (int i = 0; i < args.length; i++) {
            Arg a = argsMap.get(args[i].toLowerCase());
            if (a == null) {
                System.err.println("Unknown argument " + args[i]);
                System.exit(1);
            }
            String value = a.not_boolean ? args[++i] : "true";
            a.value = value;
        }

        int nsteps = 0;
        File file = new File(getArg("input"));
        Set<String> formats = new HashSet<>();
        formats.add("txt");

        if (!file.exists()) {
            System.err.println(file + " not found.");
            System.exit(1);
        }

        String output = getArg("output");

        if (getArg("nsteps") != null) {
            nsteps = Integer.parseInt(getArg("nsteps"));
        }

        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
        Container container = importController.importFile(file);
        Graph g;
        if (!getArg("directed").equalsIgnoreCase("true")) {
            container.getLoader().setEdgeDefault(EdgeDirectionDefault.UNDIRECTED);
            g = graphModel.getUndirectedGraph();
        } else {
            container.getLoader().setEdgeDefault(EdgeDirectionDefault.DIRECTED);
            g = graphModel.getDirectedGraph();
        }
        importController.process(container, new DefaultProcessor(), workspace);

        // visible view
        YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
        layout.setGraphModel(graphModel);
        layout.resetPropertiesValues();

        layout.setOptimalDistance(optimalDistance);
        layout.setStep(step);
        layout.setStepRatio(stepRatio);
        layout.setInitialStep(initialStep);
        layout.setConverged(converged);
        layout.setBarnesHutTheta(barnesHutTheta);
        layout.setAdaptiveCooling(adaptiveCooling);
        layout.setQuadTreeMaxLevel(quadTreeMaxLevel);
        layout.setRelativeStrength(relativeStrength);
        layout.setConvergenceThreshold(convergenceThreshold);

        layout.initAlgo();
        for (int i = 0; i < nsteps && layout.canAlgo(); i++) {
            layout.goAlgo();
        }
        layout.endAlgo();

        writeOutput(g, true, formats, output);
    }

    private static class Arg {
        String flag;
        String description;
        boolean not_boolean;
        String defaultValue;
        String value;

        private Arg(String flag, String description, boolean not_boolean, String defaultValue) {
            this.flag = flag;
            this.description = description;
            this.not_boolean = not_boolean;
            this.defaultValue = defaultValue;
            if (defaultValue != null) {
                this.value = defaultValue;
            }
        }

        public String toString() {
            return flag;
        }
    }
}
