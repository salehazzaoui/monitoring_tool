package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.Component;
import models.Csp;
import models.Port;
import utils.FDRVerification;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewGlobalCspController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField expressionTextField;

    public void addGlobalCsp(Component component){
        String name = nameTextField.getText();
        String expression = expressionTextField.getText();
        if(name.isEmpty() || expression.isEmpty()){
            this.showAlert("Validation Error", "All field are required!", Alert.AlertType.ERROR);
            return;
        }

        Csp globalExp = new Csp(name, expression);
        component.setGlobalCsp(globalExp);
        /**
         * TODO: need to check later
         * this.checkCspStructureOfComponent(name, expression);
         * */
        this.checkCspBehaviorOfComponent(component);

    }

    private void checkCspStructureOfComponent(String name, String expression){
        Pattern patternName = Pattern.compile("([a-zA-Z]+[0-9]*)");
        Matcher matcherName = patternName.matcher(name);

        String ptrCsp = "([a-zA-Z]+[0-9]*[?!][a-zA-Z]+[0-9]*(?:[-]>[a-zA-Z]+[0-9]*[?!][a-zA-Z]+[0-9]*)*)[-]>\\\\(([a-zA-Z]+[0-9]*)(?:[?!][a-zA-Z]+[0-9]*[-]>[A-Z]+)?((?:(?:\\\\|~\\\\||\\\\[\\\\]|;|[|]{3})[a-zA-Z]+[0-9]*(?:[?!][a-zA-Z]+[0-9]*[-]>[A-Z]+)?)*)\\\\)";
        Pattern pm = Pattern.compile(ptrCsp);
        Matcher matcher = pm.matcher(expression);

        if ((!matcher.matches()) || (!matcherName.matches())) {
            this.showAlert("Validation Error", "Invalid CSP expression!", Alert.AlertType.ERROR);
            return;
        }
    }

    private void checkCspBehaviorOfComponent(Component component){
        File fileCSP = new File("testFDR.csp");
        String Text ="";
        String channels ="";
        String AllFormulla ="";
        String formullaConcat ="";
        String GlobaleFormulla ="GlobFormulla = ";
        String generatorAuto ="";
        List<Port> ports = component.getPorts();

        try{
            if(ports.size() == 0){
                this.showAlert("Validation Error", "No port in ths component!", Alert.AlertType.ERROR);
                return;
            }
            for (int i = 0; i < ports.size(); i++) {
                Port port = ports.get(i);
                formullaConcat += component.getName() + "_" + port.getCspExpression().getName() + ",";
                channels += "channel " + port.getCspExpression().getName() + " : {0} \n";
                // si le port out ou in je fait des modif sur la formulla csp
                String[] tabIn = port.getCspExpression().getExpression().split("->");

                String modBoucle = component.getName() + "_" + tabIn[tabIn.length-1];
                String restTab ="";
                Pattern portNamePattern = Pattern.compile("([a-zA-Z]+[0-9]*)\\!([a-zA-Z]+[0-9]*)");
                for(int d = 0 ; d<tabIn.length-1;d++){
                    Matcher portN=portNamePattern.matcher(tabIn[d]);
                    if(portN.matches()){
                        String portName = portN.group(1);
                        restTab+=portName+"!0->";
                    }
                    else {
                        restTab+=tabIn[d]+"->";
                    }
                }

                restTab+=modBoucle;
                port.setCspExpressionModify(new Csp(port.getCspExpression().getName(),restTab));
                AllFormulla += component.getName() + "_" + port.getCspExpression().getName() + " = "+restTab+"\n";

            }

            // Put ||| between CSP expressions ( Global CSP expression )
            String [] TabNameForm= formullaConcat.split(",");
            if(TabNameForm.length==1) {
                GlobaleFormulla += TabNameForm[0];generatorAuto += TabNameForm[0];
            } else {
                GlobaleFormulla +=TabNameForm[0];
                generatorAuto += TabNameForm[0];
                for (int m = 1 ; m< TabNameForm.length; m++){
                    GlobaleFormulla += " ||| "+TabNameForm[m];
                    generatorAuto += " ||| "+TabNameForm[m];
                }
            }


            String GlobalFormComponenet ="";
            String Assert ="";
            ArrayList<String> list1 = new ArrayList<>();
            ArrayList<String> list2 = new ArrayList<>();
            ArrayList<String> list3 = new ArrayList<>();
            if(component.getGlobalCsp().getExpression()==" "){
                System.out.println("hediiiiiiiiiiiiiiiiii "+generatorAuto);
                component.setGlobalCsp(new Csp("autoFormula",generatorAuto));
                GlobalFormComponenet =  "autoFormula  = " +generatorAuto;
                Assert = "assert GlobFormulla [T= autoFormula";
            }
            else{

                String formuleglobaleUser = component.getGlobalCsp().getExpression();
                Pattern formulaflesh = Pattern.compile("([a-zA-Z]+[0-9]*[!][a-zA-Z]+[0-9]*)(?=[-]>)");
                Pattern formulaExclam = Pattern.compile("([a-zA-Z]+[0-9]*)(?=!)");
                Matcher formulafleshMatcher = formulaflesh.matcher(formuleglobaleUser);
                while(formulafleshMatcher.find()){
                    list1.add(formulafleshMatcher.group(1));
                }
                for(int s=0;s<list1.size();s++){
                    Matcher formulaExclamhMatcher = formulaExclam.matcher(list1.get(s));
                    while(formulaExclamhMatcher.find()) {
                        list2.add(formulaExclamhMatcher.group(1));
                    }
                }
                for (int k =0;k<list2.size();k++){
                    list3.add(list2.get(k)+"!0");
                }
                for(int d =0; d<list1.size();d++){
                    formuleglobaleUser = formuleglobaleUser.replaceAll(list1.get(d),list3.get(d));
                }

                GlobalFormComponenet = component.getGlobalCsp().getName()+"="+formuleglobaleUser+"\n";
                Assert = "assert GlobFormulla [T= "+component.getGlobalCsp().getName();
            }

            // Fill CSP file
            Text = channels+"\n"+AllFormulla+"\n"+GlobalFormComponenet+"\n"+GlobaleFormulla+"\n\n"+Assert;
            FileWriter ffw = new FileWriter(fileCSP);
            ffw.write(Text);
            ffw.flush();
            if(FDRVerification.testFDR("testFDR.csp")[0])  {
                this.showAlert("Test FDR", "Test succeed!", Alert.AlertType.INFORMATION);
            }
            else {
                this.showAlert("Test FDR", "Test failed!", Alert.AlertType.ERROR);
            }
        }
        catch(Exception e1){
            e1.printStackTrace();
        }
    }

    private void showAlert(String title, String body, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(body);
        alert.show();
    }

    public void initNameTextField(String name) {
        nameTextField.setText(name);
    }

    public void initExpressionTextField(String exp) {
        expressionTextField.setText(exp);
    }
}
