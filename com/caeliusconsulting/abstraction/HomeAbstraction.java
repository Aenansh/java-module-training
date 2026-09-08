package com.caeliusconsulting.abstraction;

abstract class SmartHomeController {
  protected final String devicedId;
  public boolean isOn;
  protected int usage;

  public SmartHomeController(String id) {
    devicedId = id;
    usage = 0;
  }

  abstract public void turnOn();

  abstract public void turnOff();

  public void getPowerUsage() {
    System.out.println("Your total usage activity is " + usage);
  }
}

class SmartLight extends SmartHomeController {
  SmartLight(String id) {
    super(id);
  }

  public void turnOn() {
    if (!isOn) {
      System.out.println("Your light is turned on.");
      usage++;
      isOn = true;
    }
  }

  public void turnOff() {
    if (isOn) {
      System.out.println("Your light is turned off.");
      isOn = false;
    }
  }
}

class SmartFan extends SmartHomeController {
  SmartFan(String id) {
    super(id);
  }

  public void turnOn() {
    if (!isOn) {
      System.out.println("Your fan is turned on.");
      usage += 2;
      isOn = true;
    }
  }

  public void turnOff() {
    if (isOn) {
      System.out.println("Your fan is turned off.");
      isOn = false;
    }
  }
}

class SmartThermostat extends SmartHomeController {
  SmartThermostat(String id) {
    super(id);
  }

  public void turnOn() {
    if (!isOn) {
      System.out.println("Your thermostat is turned on.");
      usage += 3;
      isOn = true;
    }
  }

  public void turnOff() {
    if (isOn) {
      System.out.println("Your thermostat is turned off.");
      isOn = false;
    }
  }
}

public class HomeAbstraction {
  public static void main(String[] args) {
    SmartHomeController[] devices = {
        new SmartLight("light-101"),
        new SmartFan("fan-202"),
        new SmartThermostat("thermostat-303")
    };

    for (SmartHomeController device : devices) {
      device.turnOn();
      device.getPowerUsage();
      device.turnOff();
      System.out.println();
    }
  }
}
