package edu.gw.csci.simulator.utils;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TextAreaAppender for Log4j 2
 */
@Plugin(name = "ConsoleAppender", category = "Core", elementType = "appender", printObject = true)
public class ConsoleAppender extends AbstractAppender {

    private static TextFlow textFlow;
    private static ScrollPane scrollPane;

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();

    private final HashMap<Level, Color> colorMap = new HashMap<>();

    public ConsoleAppender(String name, Filter filter, Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        colorMap.put(Level.ERROR, Color.RED);
        colorMap.put(Level.WARN, Color.GOLD);
    }

    /**
     * This method is where the appender does the work.
     *
     * @param event Log event with log data
     */
    @Override
    public void append(LogEvent event) {
        readLock.lock();
        final String message = new String(getLayout().toByteArray(event));
        final Level level = event.getLevel();
        try {
            Platform.runLater(() -> {
                try {
                    if (textFlow != null) {
                        Text text = new Text(message);
                        text.setFill(getColor(level));
                        textFlow.getChildren().add(text);
                    }
                } catch (final Throwable t) {
                    System.out.println("Error while append to TextArea: " + t.getMessage());
                }
            });
        } catch (final IllegalStateException ex) {
            ex.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    private Color getColor(Level level) {
        return colorMap.getOrDefault(level, Color.BLACK);
    }

    /**
     * Factory method. Log4j will parse the configuration and call this factory
     * method to construct the appender with
     * the configured attributes.
     *
     * @param name   Name of appender
     * @param layout Log layout of appender
     * @param filter Filter for appender
     * @return The TextAreaAppender
     */
    @PluginFactory
    public static ConsoleAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter) {
        if (name == null) {
            LOGGER.error("No name provided for TextAreaAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new ConsoleAppender(name, filter, layout, true);
    }


    /**
     * Set TextArea to append
     *
     * @param textFlow TextArea to append
     */
    public static void setTextFlow(TextFlow textFlow) {
        textFlow.getChildren().addListener((ListChangeListener<Node>) ((change) -> {
            scrollPane.layout();
            scrollPane.setVvalue(1.0f);
        }));
        ConsoleAppender.textFlow = textFlow;
    }

    public static void setScrollPane(ScrollPane scrollPane) {
        ConsoleAppender.scrollPane = scrollPane;
    }

    /**
     * Clears the log
     */
    public static void clear() {
        textFlow.getChildren().clear();
    }
}