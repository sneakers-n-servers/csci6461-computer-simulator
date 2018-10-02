package edu.gw.csci.simulator.gui;

import edu.gw.csci.simulator.utils.Bits;
import edu.gw.csci.simulator.isa.Execute;
import edu.gw.csci.simulator.memory.Memory;
import edu.gw.csci.simulator.memory.MemoryDecorator;
import edu.gw.csci.simulator.registers.*;
import edu.gw.csci.simulator.utils.ConsoleAppender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;
import java.util.regex.Pattern;

    /**
     * This class "connects" the GUI to the simulated computer.
     * It translates user events to machine operations and vice-versa.
     *
     * @version 20180918
     */
public class Controller {

    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

    @FXML
    private TableView<Register> registerTable;

    @FXML
    private TableColumn<Register, String> registerNameColumn;

    @FXML
    private TableColumn<Register, String> binaryColumn;

    @FXML
    private TableColumn<Register, String> decimalColumn;

    @FXML
    private Button store;

    @FXML
    private Button execute;

    @FXML
    private TextField IRinput;

    @FXML
    private TextField memoryADDR;

    @FXML
    private TextField memoryVALUE;

    @FXML
    private TextArea developerLog;

    @FXML
    private ComboBox<String> logLevels = new ComboBox<>();

    private AllRegisters allRegisters;
    private Memory memory;

    @FXML
    protected void runIPL() {
        LOGGER.info("Initializing machine");
        allRegisters.initializeRegisters();
    }

    public Controller(){
        this.allRegisters = new AllRegisters();
        this.memory = new Memory();
    }

    @FXML
    private void initialize(){
        initializeRegisters();
        initializeMemory();
        handleLogs(Level.INFO);
    }

    private void initializeRegisters(){
        registerNameColumn.setCellValueFactory(cellData -> new RegisterDecorator(cellData.getValue()).getRegisterName());
        binaryColumn.setCellValueFactory(cellData -> new RegisterDecorator(cellData.getValue()).toBinaryObservableString());
        decimalColumn.setCellValueFactory(cellData -> new RegisterDecorator(cellData.getValue()).toLongObservableString());

        ObservableList<Register> registerList = FXCollections.observableArrayList();
        for(Map.Entry<RegisterType, Register> registerEntry: allRegisters.getRegisters()){
            registerList.add(registerEntry.getValue());
        }
        registerTable.setItems(registerList);
        allRegisters.bindTableView(registerTable);
    }

    private void initializeMemory() {
        memory.initialize();
    }

    private void handleLogs(Level level){
        //Set the default list of log levels
        String[] logValues = Arrays.stream(Level.values())
                .map(Level::name)
                .toArray(String[]::new);
        Arrays.sort(logValues);
        logLevels.getItems().addAll(logValues);
        logLevels.setOnAction(event -> {
            Level currentLevel = Level.valueOf(logLevels.getValue());
            Configurator.setRootLevel(currentLevel);
        });
        logLevels.setValue(level.toString());

        //Set the text field to append to
        ConsoleAppender.setTextArea(developerLog);
    }

    @FXML
    void execute(ActionEvent event) {
        if (IRinput.getText().length() == 16) {
            String IRinputS = IRinput.getText().toString();
            Register IR = allRegisters.getRegister(RegisterType.IR);
            if (isBinary(IRinputS)) {
                int IRinputI = Integer.parseInt(IRinputS, 2);
                BitSet bs = Bits.convert(IRinputI);
                IR.setData(bs);
                Execute.excute_IR(allRegisters, memory);
                registerTable.refresh();
            }
        }
    }

    @FXML
    void MemoryStore(ActionEvent event) {
        int memoryADDRS = Integer.parseInt(memoryADDR.getText());
        int memoryVALUES =Integer.parseInt(memoryVALUE.getText());
        MemoryDecorator memoryDecorator = new MemoryDecorator(memory, allRegisters);
        memoryDecorator.store(memoryVALUES, memoryADDRS);
    }

    public static boolean isBinary(String str){
        Pattern pattern = Pattern.compile("[0-1]*");
        return pattern.matcher(str).matches();
    }
}
