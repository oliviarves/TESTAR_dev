package org.testar;

import es.upv.staq.testar.serialisation.LogSerialiser;
import es.upv.staq.testar.serialisation.ScreenshotSerialiser;
import es.upv.staq.testar.serialisation.TestSerialiser;
import org.fruit.alayer.*;
import org.fruit.monkey.ConfigTags;
import org.fruit.monkey.Settings;

import java.util.Set;

import static org.fruit.alayer.Tags.*;
import static org.fruit.alayer.Tags.SystemState;

public class ReplayableSequences {

    /**
     * This method initializes the fragment for replayable sequence
     *
     * @param state
     */
    public static void initFragmentForReplayableSequence(State state, Taggable fragment){
        // Fragment is used for saving a replayable sequence:
        fragment = new TaggableBase();
        fragment.set(SystemState, state);
        Verdict verdict = state.get(OracleVerdict, Verdict.OK);
        fragment.set(OracleVerdict, verdict);
    }

    /**
     * Saving the action into the fragment for replayable sequence
     *
     * @param action
     */
    public static void saveActionIntoFragmentForReplayableSequence(Action action, State state, Set<Action> actions, Taggable fragment, Verdict processVerdict, Settings settings) {
        Verdict verdict = state.get(OracleVerdict, Verdict.OK);
        fragment.set(OracleVerdict, verdict.join(processVerdict));
        fragment.set(ExecutedAction,action);
        fragment.set(ActionSet, actions);
        fragment.set(ActionDuration, settings.get(ConfigTags.ActionDuration));
        fragment.set(ActionDelay, settings.get(ConfigTags.TimeToWaitAfterAction));
        LogSerialiser.log("Writing fragment to sequence file...\n",LogSerialiser.LogLevel.Debug);
        TestSerialiser.write(fragment);
        //resetting the fragment:
        fragment =new TaggableBase();
        fragment.set(SystemState, state);
    }


    /**
     * Saving the action into the fragment for replayable sequence
     *
     * @param state
     */
    public static void saveStateIntoFragmentForReplayableSequence(State state, Taggable fragment, Verdict processVerdict, Settings settings) {
        Verdict verdict = state.get(OracleVerdict, Verdict.OK);
        fragment.set(OracleVerdict, verdict.join(processVerdict));
        fragment.set(ActionDuration, settings.get(ConfigTags.ActionDuration));
        fragment.set(ActionDelay, settings.get(ConfigTags.TimeToWaitAfterAction));
        LogSerialiser.log("Writing fragment to sequence file...\n",LogSerialiser.LogLevel.Debug);
        TestSerialiser.write(fragment);
        //resetting the fragment:
        fragment =new TaggableBase();
        fragment.set(SystemState, state);
    }

    /**
     * Writing the fragment into file and closing the test serialiser
     */
    public static void writeAndCloseFragmentForReplayableSequence(Taggable fragment) {
        //closing ScreenshotSerialiser:
        ScreenshotSerialiser.finish();
        LogSerialiser.log("Writing fragment to sequence file...\n", LogSerialiser.LogLevel.Debug);
        TestSerialiser.write(fragment);

        //Wait since TestSerialiser write all fragments on sequence File
        while(!TestSerialiser.isSavingQueueEmpty() && !ScreenshotSerialiser.isSavingQueueEmpty()) {
            //System.out.println("Saving sequences...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        TestSerialiser.finish();
        LogSerialiser.log("Wrote fragment to sequence file!\n", LogSerialiser.LogLevel.Debug);
    }
}
