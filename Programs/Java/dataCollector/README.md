#The Water Work's Data Collection application

##Program Usage
The java program uses Apache's CLI library to manage command line parameters.  Run program without parameters to see a list of options.

`--port <commPort>` - is the serial port to read meter data from.  A port of `STUB` uses a disconnected dummy meter reader.
`--sensorBus <sensorBus>` - is the type of bus where the sensors can be read.  A bus of `STUB` is a disconnected bus.  `I2C` is the Raspberry Pi's I2C interface and the slave address (as a hexadecimal string) must also be provided.

##Program notes
The target platform for this application is a Raspberry Pi, but should be "runnable" on any platform at least in a test configuration.  Use of command line parameters will be used to setup or configure services at runtime.  

The application will stand up three service components.  
1. Meter Reading component - This defines the component to read from a Grid Insight OV-1 or simular device.
1. Sensor Reading component - This defined the component to read sensor information.
1. Publishing component - This defines an endpoint and configuration to which to send the data to.


##Change Log And Implementation Notes

0.0.1
- Just beginning

GET https://api.thingspeak.com/update?api_key=W7BI5H4Z0CMJAUNU&field1=0&field2=0