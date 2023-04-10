# Monitoring Tool
This monitoring tool to manage the dynamic reconfiguration of software architecture at runtime.

## To get the project running
- First include the javaFx sdk jar.
- Add Vm option to your run configuration
```
--module-path "\path\to\javafx-sdk-11\lib" --add-modules javafx.controls,javafx.fxml
```
- Include the Uppaal.jar and all jars in lib folder of uppaal

## To make uppaal works
To make uppaal run when you click on check with 
uppaal change the uppaalPath variable and modelPath
in utils.EV class depend on your uppaal path and your project path