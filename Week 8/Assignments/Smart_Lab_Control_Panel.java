import java.util.*;

interface Capability {
    String getName();
    boolean execute(String value, String deviceName);
}

class PowerCapability implements Capability {
    private boolean on;

    public String getName() {
        return "Power";
    }

    public boolean execute(String value, String deviceName) {
        if (!value.equalsIgnoreCase("ON") && !value.equalsIgnoreCase("OFF")) {
            System.out.println("Rejected: " + deviceName + " power must be ON or OFF.");
            return false;
        }
        on = value.equalsIgnoreCase("ON");
        System.out.println(deviceName + ": " + (on ? "ON" : "OFF") + ".");
        return true;
    }
}

class BrightnessCapability implements Capability {
    private int brightness;

    public String getName() {
        return "Brightness";
    }

    public boolean execute(String value, String deviceName) {
        try {
            int v = Integer.parseInt(value);
            if (v < 0 || v > 100) {
                System.out.println("Rejected: " + deviceName + " brightness must be between 0 and 100%.");
                return false;
            }
            brightness = v;
            System.out.println(deviceName + ": brightness set to " + brightness + "%.");
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Rejected: invalid brightness value.");
            return false;
        }
    }
}

class TemperatureCapability implements Capability {
    private int temperature;

    public String getName() {
        return "Temperature";
    }

    public boolean execute(String value, String deviceName) {
        try {
            int v = Integer.parseInt(value);
            if (v < 16 || v > 30) {
                System.out.println("Rejected: " + deviceName + " temperature must be between 16°C and 30°C.");
                return false;
            }
            temperature = v;
            System.out.println(deviceName + ": temperature set to " + temperature + "°C.");
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Rejected: invalid temperature value.");
            return false;
        }
    }
}

class Device {
    private final String name;
    private final Map<String, Capability> capabilities = new LinkedHashMap<>();

    public Device(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addCapability(Capability capability) {
        capabilities.put(capability.getName(), capability);
        System.out.println(name + ": " + capability.getName() + " capability added.");
    }

    public boolean apply(String capabilityName, String value) {
        Capability capability = capabilities.get(capabilityName);
        if (capability == null) {
            return false;
        }
        return capability.execute(value, name);
    }

    public boolean hasCapability(String capabilityName) {
        return capabilities.containsKey(capabilityName);
    }
}

class SceneStep {
    private final String capability;
    private final String value;

    public SceneStep(String capability, String value) {
        this.capability = capability;
        this.value = value;
    }

    public String getCapability() {
        return capability;
    }

    public String getValue() {
        return value;
    }
}

class Scene {
    private final String name;
    private final List<SceneStep> steps;

    public Scene(String name, List<SceneStep> steps) {
        this.name = name;
        this.steps = new ArrayList<>(steps);
    }

    public void execute(List<Device> devices) {
        System.out.println("Scene '" + name + "' started.");
        int applied = 0;
        for (SceneStep step : steps) {
            for (Device device : devices) {
                if (device.hasCapability(step.getCapability())) {
                    if (device.apply(step.getCapability(), step.getValue())) {
                        applied++;
                    }
                }
            }
        }
        System.out.println("Scene '" + name + "' completed: " + applied + " actions applied.");
    }
}

public class Smart_Lab_Control_Panel {
    public static void main(String[] args) {
        Device ac = new Device("Lab AC");
        ac.addCapability(new PowerCapability());
        ac.addCapability(new TemperatureCapability());

        Device lights = new Device("Ceiling Lights");
        lights.addCapability(new PowerCapability());
        lights.addCapability(new BrightnessCapability());

        Device projector = new Device("Projector");
        projector.addCapability(new PowerCapability());

        List<Device> devices = Arrays.asList(ac, lights, projector);
        Scene scene = new Scene("Lecture Mode", Arrays.asList(
                new SceneStep("Power", "ON"),
                new SceneStep("Brightness", "40"),
                new SceneStep("Temperature", "24")
        ));
        scene.execute(devices);

        ac.apply("Temperature", "12");
        projector.addCapability(new BrightnessCapability());
        projector.apply("Brightness", "70");
    }
}
