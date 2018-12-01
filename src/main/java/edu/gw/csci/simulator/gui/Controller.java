package edu.gw.csci.simulator.gui;

import edu.gw.csci.simulator.cpu.CPU;
import edu.gw.csci.simulator.cpu.SimulatorFileReader;
import edu.gw.csci.simulator.cpu.TrapController;
import edu.gw.csci.simulator.exceptions.SimulatorException;
import edu.gw.csci.simulator.memory.*;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterDecorator;
import edu.gw.csci.simulator.registers.RegisterType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
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
    private TableColumn<Register, String> registerNameColumn,
            registerDescriptionColumn,
            registerBinaryColumn,
            registerDecimalColumn;

    @FXML
    private Pagination memoryPagination;

    @FXML
    private TableView<MemoryChunk> memoryTable;

    @FXML
    private TableColumn<MemoryChunk, String> memoryIndexColumn,
            memoryBinaryColumn,
            memoryDecimalColumn;

    @FXML
    private ComboBox<String> programNameSelector;

    @FXML
    private TextArea programContents;

    @FXML
    private TextArea console;

    @FXML
    private Spinner<Integer> memorySpinner;

    @FXML
    private TextField startIndex;

    @FXML
    private TextField InputWord;

    private AllRegisters allRegisters;
    private Memory memory;
    private HashMap<String, Program> programs;
    private CPU cpu;

    private boolean initialized;

    private TrapController trapController;

    /**
     * Both memory and registers are initialized to their default values.
     * The reserved memory location 1 is set to 6, as to execute a halt
     * if a trap instruction occurs. The boolean initialized is also set
     * as to provide proper indication to other console settings that values can be
     * set without throwing null pointers. We can safely set the memory here because it
     * is developer driven, and there is no need to check for errors.
     */
    @FXML
    protected void runIPL() {
        LOGGER.info("Initializing simulator");
        allRegisters.initialize();
        memory.initialize();
        initializeCPU();
        programContents.clear();
        console.clear();
        programNameSelector.setValue("FreeRun");
        initialized = true;
        trapController.setDefaultExceptionTable();
    }

    public Controller() {
        this.allRegisters = new AllRegisters();
        this.memory = new Memory();
        this.programs = new HashMap<>();

        AllMemory allMemory = new AllMemory(memory, allRegisters, new MemoryCache());
        CPU cpu = new CPU(allMemory);
        this.cpu = cpu;
        trapController = new TrapController(cpu);
        SimulatorException.setTrapController(trapController);
    }

    @FXML
    private void initialize() {
        initializeRegisters();
        initializeMemory();
        initializePrograms();
        trapController.setTables(registerTable, memoryTable);
    }

    private void initializeCPU() {
        //initialize Input and Output
        cpu.consoleInput.clear();
        cpu.consoleOutput.clear();
        //initialize FileReader
        SimulatorFileReader.initializeFileReader();
    }

    /**
     * This function performs the proper binding of the registers to the GUI. Each register column is set with a value
     * factory that converts the {@link Register} to a desired format. The initial list of registers used to populate the
     * table is generated and set. Finally, each register value is bound to refresh the table when modified.
     * This provides convenience throughout the simulators operation. Registers are modifiable within the console itself.
     */
    private void initializeRegisters() {
        registerNameColumn.setCellValueFactory(cellData -> new RegisterDecorator(cellData.getValue()).getRegisterName());
        registerDescriptionColumn.setCellValueFactory(cellData -> new RegisterDecorator(cellData.getValue()).getRegisterDescription());

        registerBinaryColumn.setCellValueFactory(cellData -> new BitDecorator<>(cellData.getValue()).toBinaryObservableString());
        registerBinaryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        registerBinaryColumn.setOnEditCommit((TableColumn.CellEditEvent<Register, String> t) -> {
            if (!initialized) {
                LOGGER.error("Initialize the machine before editing");
                registerTable.refresh();
                return;
            }
            Register register = t.getTableView().getItems().get(t.getTablePosition().getRow());
            RegisterDecorator rd = new RegisterDecorator(register);
            rd.setBinaryValue(t.getNewValue());
            LOGGER.info("Setting register {} to {}", register.getName(), rd.toBinaryString());
        });

        registerDecimalColumn.setCellValueFactory(cellData -> new RegisterDecorator(cellData.getValue()).toFloatOrIntObservableString());
        registerDecimalColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        registerDecimalColumn.setOnEditCommit((TableColumn.CellEditEvent<Register, String> t) -> {
            if (!initialized) {
                LOGGER.error("Initialize the machine before editing");
                registerTable.refresh();
                return;
            }
            Register register = t.getTableView().getItems().get(t.getTablePosition().getRow());
            RegisterDecorator rd = new RegisterDecorator(register);

            //Handle setting floats and ints
            if(RegisterType.getFloatingPointTypes().contains(register.getRegisterType())){
                rd.setFloatValue(t.getNewValue());
            }else{
                rd.setIntegerValue(t.getNewValue());
            }

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
     * This provides convenience throughout the simulator's operation.  Memory is modifiable within the console itself.
     */
    private void initializeMemory() {
        memoryIndexColumn.setCellValueFactory(cellData -> new MemoryChunkDecorator(cellData.getValue()).getIndex());
        memoryIndexColumn.setComparator(Comparator.comparingInt(Integer::parseInt));
        memoryBinaryColumn.setCellValueFactory(cellData -> new BitDecorator<>(cellData.getValue()).toBinaryObservableString());
        memoryBinaryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        memoryBinaryColumn.setOnEditCommit((TableColumn.CellEditEvent<MemoryChunk, String> t) -> {
            if (!initialized) {
                LOGGER.error("Initialize the machine before editing");
                memoryTable.refresh();
                return;
            }
            int tablePosition = t.getTablePosition().getRow();
            if (tablePosition == TrapController.HALT_LOCATION) {
                LOGGER.warn("Index 6 is reserved for halt during trap codes, this is not recomended");
            }
            MemoryChunk mem = t.getTableView().getItems().get(tablePosition);
            MemoryChunkDecorator md = new MemoryChunkDecorator(mem);
            md.setBinaryValue(t.getNewValue());
            LOGGER.debug("Setting memory location {} to {}", md.getIndex().toString(), md.toBinaryString());
            memoryTable.refresh();
        });

        memoryDecimalColumn.setCellValueFactory(cellData -> new BitDecorator<>(cellData.getValue()).toLongObservableString());
        memoryDecimalColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        memoryDecimalColumn.setOnEditCommit((TableColumn.CellEditEvent<MemoryChunk, String> t) -> {
            if (!initialized) {
                LOGGER.error("Initialize the machine before editing");
                memoryTable.refresh();
                return;
            }
            int tablePosition = t.getTablePosition().getRow();
            if (tablePosition == TrapController.HALT_LOCATION) {
                LOGGER.warn("Index 6 is reserved for halt during trap codes, this is not recomended");
            }
            MemoryChunk mem = t.getTableView().getItems().get(tablePosition);
            MemoryChunkDecorator md = new MemoryChunkDecorator(mem);
            md.setIntegerValue(t.getNewValue());
            LOGGER.debug("Setting memory location {} to {}", md.getIndex().toString(), md.toInt());
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

        memorySpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                int i;
                try {
                    i = Integer.parseInt(newValue);
                    if (i >= memory.getSize()) {
                        return;
                    }
                } catch (NumberFormatException e) {
                    return;
                }
                Platform.runLater(() -> {
                    memoryPagination.currentPageIndexProperty().setValue(i / 32);
                    memoryTable.scrollTo(i % 32);
                    memoryTable.getSelectionModel().select(i % 32);
                });
            }
        });
    }

    /**
     * This function initializes {@link Program}s within the simulator. The default
     * program is named FreeRun, which initializes empty, and left to the console
     * user to define. Switching between programs clears the contents of the text area
     * and loads the contents of newly selected program.
     */
    private void initializePrograms() {
        //We need to pre-populate Program1, so this code will change
        Program freeRun = new Program("FreeRun");
        programs.put("FreeRun", freeRun);

        Program program1 = new Program("Program1");
        programs.put("Program1", program1);
        PreStoreProgram.SetProgram1(program1);

        Program program2 = new Program("Program2");
        programs.put("Program2", program2);
        PreStoreProgram.SetProgram2(program2);

        Program programls = new Program("Programls");
        programs.put("Programls", programls);
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
            for (String line : selected.getLines()) {
                programContents.appendText(line + "\n");
            }
        });
    }

    @FXML
    private void runProgram() {
        if (!initialized) {
            LOGGER.error("Initialize the machine before running");
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
    private void stepProgram() {
        if (!initialized) {
            LOGGER.error("Initialize the machine before stepping");
            return;
        }
        cpu.step();
        SetconsoleOutput();
    }

    @FXML
    private void loadProgram() {
        if (!initialized) {
            LOGGER.error("Initialize the machine before loading");
            return;
        }

        LOGGER.info("Loading Program");
        String programName = programNameSelector.getValue();
        Program program = programs.get(programName);
        cpu.setProgram(program);
        if (!startIndex.getText().isEmpty()) {
            int start = Integer.parseInt(startIndex.getText());
            cpu.loadProgram(start);
        } else {
            cpu.loadProgram();
        }
    }

    /**
     * We define a save program routine to overcome the fact that JavaFX wants
     * to execute a routine every time the program's contents are modified. Adding a timer
     * would resolve this issue, but doing so adds a complication with little
     * benefit. Having an explicit save function makes sense, and overcomes this hurdle.
     */
    @FXML
    private void saveProgram() {
        String name = programNameSelector.getValue();
        LOGGER.info(String.format("Saving the contents of program %s", name));
        String contents = programContents.textProperty().get();
        Program program = programs.get(name);
        program.clearContents();
        String[] lines = contents.split("\n");
        for (String line : lines) {
            program.appendLine(line);
        }
    }

    @FXML
    private void input() {
        String[] lines = console.textProperty().get().split("\n");
        for (String line : lines) {
            if (isNumeric(line)) {
                cpu.consoleInput.add(line.replace(" ", ""));
            }
        }
        console.clear();
    }

    public void SetconsoleOutput() {
        if (!cpu.consoleOutput.isEmpty()) {
            console.clear();

            for (String line : cpu.consoleOutput)
                console.appendText(line + "\n\r");
        }
    }

    @FXML
    private void LoadProgram1() {
        //only use to load program1
        LOGGER.info("Loading Program1");
        Program program = programs.get("Program1");
        cpu.setProgram(program);
        cpu.loadProgram(126);
    }

    @FXML
    private void LoadProgram2() {
        //only use to load program2
        LOGGER.info("Loading Program2");
        Program program = programs.get("Program2");
        cpu.setProgram(program);
        cpu.loadProgram(64);
    }

    @FXML
    private void PreStoreMemoryForProgram1() {
        //only use to store some value to memory to run program1
        PreStoreMemory.PreStoreMemoryForProgram1(cpu);
    }

    @FXML
    private void PreStoreMemoryForProgram2() {
        //only use to store some value to memory to run program2
        PreStoreMemory.PreStoreMemoryForProgram2(cpu);
    }


    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]*");
        return pattern.matcher(str).matches();
    }

    @FXML
    private void ReadFile() {
        cpu.FileReader();
        SetconsoleOutput();
    }

    @FXML
    private void InputAWord() {
        String word = InputWord.getText().toLowerCase();
        if (SimulatorFileReader.WordMap.containsKey(word)) {
            cpu.consoleOutput.add(word);
        } else {
            SimulatorFileReader.encode(word);
        }
        cpu.consoleInput.add(String.valueOf(SimulatorFileReader.getCode(word)));
    }
}
