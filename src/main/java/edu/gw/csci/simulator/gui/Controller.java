package edu.gw.csci.simulator.gui;

import edu.gw.csci.simulator.cpu.CPU;
import edu.gw.csci.simulator.exceptions.SimulatorException;
import edu.gw.csci.simulator.memory.Memory;
import edu.gw.csci.simulator.memory.MemoryCache;
import edu.gw.csci.simulator.memory.MemoryChunk;
import edu.gw.csci.simulator.memory.MemoryChunkDecorator;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterDecorator;
import edu.gw.csci.simulator.registers.RegisterType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
    private TableColumn<Register, String> registerBinaryColumn;

    @FXML
    private TableColumn<Register, String> registerDecimalColumn;

    @FXML
    private Pagination memoryPagination;

    @FXML
    private TableView<MemoryChunk> memoryTable;

    @FXML
    private TableColumn<MemoryChunk, String> memoryIndexColumn;

    @FXML
    private TableColumn<MemoryChunk, String> memoryBinaryColumn;

    @FXML
    private TableColumn<MemoryChunk, String> memoryDecimalColumn;

    @FXML
    private ComboBox<String> programNameSelector;

    @FXML
    private TextArea programContents;

    @FXML
    private TextArea consoleInput;

    @FXML
    private TextField startIndex;
    @FXML
    private TextField PCset;

    private AllRegisters allRegisters;
    private Memory memory;
    private MemoryCache memoryCache;
    private HashMap<String, Program> programs;
    private CPU cpu;

    private boolean initialized, loaded;

    @FXML
    protected void runIPL() {
        LOGGER.info("Initializing machine");
        allRegisters.initialize();
        memory.initialize();
        initializeCPU();
        initialized = true;
    }

    public Controller() {
        this.allRegisters = new AllRegisters();
        this.memory = new Memory();
        this.memoryCache = new MemoryCache();
        this.programs = new HashMap<>();
        CPU cpu = new CPU(memory, allRegisters, memoryCache);
        //cpu.setTextArea(consoleInput);
        this.cpu = cpu;
        SimulatorException.setMachineFaultRegister(allRegisters.getRegister(RegisterType.MFR));
    }

    @FXML
    private void initialize() {
        initializeRegisters();
        initializeMemory();
        initializePrograms();
    }

    private void initializeCPU(){
        cpu.consoleInput.clear();
        cpu.consoleOutput.clear();
    }

    /**
     * This function performs the proper binding of the registers to the GUI. Each register column is set with a value
     * factory that converts the {@link Register} to a desired format. The initial list of registers used to populate the
     * table is generated and set. Finally, each register value is bound to refresh the table when modified.
     * This provides convenience throughout the simulators operation.
     */
    private void initializeRegisters() {
        registerNameColumn.setCellValueFactory(cellData -> new RegisterDecorator(cellData.getValue()).getRegisterName());

        registerBinaryColumn.setCellValueFactory(cellData -> new BitDecorator<>(cellData.getValue()).toBinaryObservableString());
        registerBinaryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        registerBinaryColumn.setOnEditCommit((TableColumn.CellEditEvent<Register, String> t) -> {
            Register register = t.getTableView().getItems().get(t.getTablePosition().getRow());
            RegisterDecorator rd = new RegisterDecorator(register);
            rd.setValue(t.getNewValue());
            LOGGER.info("Setting register {} to {}", register.getName(), rd.toBinaryString());
        });

        registerDecimalColumn.setCellValueFactory(cellData -> new BitDecorator<>(cellData.getValue()).toLongObservableString());
        registerDecimalColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        registerDecimalColumn.setOnEditCommit((TableColumn.CellEditEvent<Register, String> t) -> {
            Register register = t.getTableView().getItems().get(t.getTablePosition().getRow());
            RegisterDecorator rd = new RegisterDecorator(register);
            rd.setValue(t.getNewValue());
            LOGGER.info("Setting register {} to {}", register.getName(), rd.toInt());
        });

        ObservableList<Register> registerList = FXCollections.observableArrayList();
        for (Map.Entry<RegisterType, Register> registerEntry : allRegisters.getRegisters()) {
            registerList.add(registerEntry.getValue());
        }
        registerTable.setItems(registerList);
        allRegisters.bindTableView(registerTable);
    }

    /**
     * This function performs the proper binding of the memory to the GUI. Each memory column is set with a value
     * factory that converts the {@link MemoryChunk} to a desired format. The initial list of memory locations used
     * to populate the table is generated and set. Finally each memmory location is bound to refresh the table when modified.
     * This provides convenience throughout the simulator's operation.
     */
    private void initializeMemory() {
        memoryIndexColumn.setCellValueFactory(cellData -> new MemoryChunkDecorator(cellData.getValue()).getIndex());
        memoryBinaryColumn.setCellValueFactory(cellData -> new BitDecorator<>(cellData.getValue()).toBinaryObservableString());
        memoryBinaryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        memoryBinaryColumn.setOnEditCommit((TableColumn.CellEditEvent<MemoryChunk, String> t) -> {
            MemoryChunk mem = t.getTableView().getItems().get(t.getTablePosition().getRow());
            MemoryChunkDecorator md = new MemoryChunkDecorator(mem);
            md.setValue(t.getNewValue());
            LOGGER.info("Setting memory location {} to {}", md.getIndex().toString(), md.toBinaryString());
        });

        memoryDecimalColumn.setCellValueFactory(cellData -> new BitDecorator<>(cellData.getValue()).toLongObservableString());
        memoryDecimalColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        memoryDecimalColumn.setOnEditCommit((TableColumn.CellEditEvent<MemoryChunk, String> t) -> {
            MemoryChunk mem = t.getTableView().getItems().get(t.getTablePosition().getRow());
            MemoryChunkDecorator md = new MemoryChunkDecorator(mem);
            md.setValue(t.getNewValue());
            LOGGER.info("Setting memory location {} to {}", md.getIndex().toString(), md.toInt());
        });

        ObservableList<MemoryChunk> memoryList = FXCollections.observableArrayList();
        memoryList.addAll(Arrays.asList(memory.getMemory()));
        memoryTable.setItems(memoryList);
        memory.bindTableView(memoryTable);
        memoryPagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * 32;
            int toIndex = Math.min(fromIndex + 32, memory.getSize());
            memoryTable.setItems(FXCollections.observableList(memoryList.subList(fromIndex, toIndex)));
            return new BorderPane(memoryTable);
        });
        int maxPages = (int) Math.ceil((double) memory.getSize() / 32);
        memoryPagination.setPageCount(maxPages);
    }

    /**
     * This function initializes {@link Program}s within the simulator. The default
     * program is named FreeRun, which initializes empty, and left to the console
     * user to define. Switching between programs clears the contents of the text area
     * and loads the contents of newly selected program.
     */
    private void initializePrograms(){
        //We need to pre-populate Program1, so this code will change
        Program freeRun = new Program("FreeRun");
        programs.put("FreeRun", freeRun);

        Program program1 = new Program("Program1");
        programs.put("Program1", program1);
        PreStoreProgram.SetProgram1(program1);

        Program programls = new Program("Programls");
        this.programs.put("Programls", programls);
        PreStoreProgram.SetProgramLS(programls);

        //Set the ComboBox to all of our programs
        Set<String> allPrograms = programs.keySet();
        String[] programNames = new String[allPrograms.size()];
        programNames = allPrograms.toArray(programNames);
        Arrays.sort(programNames);
        programNameSelector.getItems().addAll(programNames);

        //Set the default program
        programNameSelector.setValue(freeRun.getName());

        //Handle switching programs
        programNameSelector.setOnAction(event -> {
            programContents.clear();
            String programName = programNameSelector.getValue();
            LOGGER.info(String.format("Loading program %s", programName));
            Program selected = programs.get(programName);
            for(String line : selected.getLines()){
                programContents.appendText(line + "\n");
            }
        });
    }

    @FXML
    private void runProgram(){
        if(! initialized){
            LOGGER.error("Initialize the machine before running");
            return;
        }
        if(! loaded){
            LOGGER.error("Load program before stepping");
            return;
        }

        LOGGER.info("Running Program");
        String programName = programNameSelector.getValue();
        Program program = programs.get(programName);
        cpu.setProgram(program);
        cpu.execute();
        SetconsoleOutput();
    }

    @FXML
    private void stepProgram(){
        if(! initialized){
            LOGGER.error("Initialize the machine before stepping");
            return;
        }
        if(! loaded){
            LOGGER.error("Load program before stepping");
            return;
        }
        LOGGER.info("One Step Run");
        cpu.step();
        SetconsoleOutput();
    }

    @FXML
    private void loadProgram(){
        if(! initialized){
            LOGGER.error("Initialize the machine before loading");
            return;
        }

        LOGGER.info("Loading Program");
        String programName = programNameSelector.getValue();
        Program program = programs.get(programName);
        cpu.setProgram(program);
        if(!startIndex.getText().isEmpty()) {
            int start = Integer.parseInt(startIndex.getText());
            cpu.loadProgram(start);
        }
        else{
            cpu.loadProgram();
        }
        loaded = true;
    }

    /**
     * We define a save program routine to overcome the fact that JavaFX wants
     * to execute a routine every time the program's contents are modified. Adding a timer
     * would resolve this issue, but doing so adds a complication with little
     * benefit. Having an explicit save function makes sense, and overcomes this hurdle.
     */
    @FXML
    private void saveProgram(){
        String name = programNameSelector.getValue();
        LOGGER.info(String.format("Saving the contents of program %s", name));
        String contents = programContents.textProperty().get();
        Program program = programs.get(name);
        program.clearContents();
        String[] lines = contents.split("\n");
        for(String line : lines){
            program.appendLine(line);
        }
    }
    @FXML
    private void setPC(){
        Register PC = allRegisters.getRegister(RegisterType.PC);
        RegisterDecorator PCd = new RegisterDecorator(PC);
        PCd.setValue(Integer.parseInt(PCset.getText()));
        String mess = String.format("Set PC = %d",Integer.parseInt(PCset.getText()));
        LOGGER.info(mess);
    }

    @FXML
    private void input(){
        String[] lines = consoleInput.textProperty().get().split("\n");
        for(String line : lines){
            if(isNumeric(line)) {
                cpu.consoleInput.add(line.replace(" ", ""));
            }
        }
        consoleInput.clear();
    }

    public void SetconsoleOutput()
    {
        if(!cpu.consoleOutput.isEmpty()) {
            consoleInput.clear();

            for(String line:cpu.consoleOutput)
            consoleInput.appendText(line + "\n\r");
        }
    }

    @FXML
    private void LoadProgram1(){
        //only use to store program1
        LOGGER.info("Loading Program1");
        Program program = programs.get("Program1");
        cpu.setProgram(program);
        cpu.loadProgram(126);
        loaded = true;
    }
    @FXML
    private void PreStoreMemoryForProgram1(){
        //only use to store some value to memory to run program1
        cpu.StoreValue(64,8);
        cpu.StoreValue(170,9);
        cpu.StoreValue(65535,85);
        cpu.StoreValue(64,86);
        cpu.StoreValue(84,87);
    }


    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]*");
        return pattern.matcher(str).matches();
    }
}
