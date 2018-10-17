package testOU;

/**********************************************************************************************
 *                                                                                            *
 * COPYRIGHT (2017):                                                                          *
 * Universitat Politecnica de Valencia                                                        *
 * Camino de Vera, s/n                                                                        *
 * 46022 Valencia, Spain                                                                      *
 * www.upv.es                                                                                 *
 *                                                                                            * 
 * D I S C L A I M E R:                                                                       *
 * This software has been developed by the Universitat Politecnica de Valencia (UPV)          *
 * in the context of the STaQ (Software Testing and Quality) research group: staq.dsic.upv.es *
 * This software is distributed FREE of charge under the TESTAR license, as an open           *
 * source project under the BSD3 license (http://opensource.org/licenses/BSD-3-Clause)        *                                                                                        * 
 *                                                                                            *
 **********************************************************************************************/

/**
 * Windows calculator desktop protocol
 * @author Urko Rueda Molina
 */

import java.io.File;
import java.util.Set;
import org.fruit.alayer.Action;
import org.fruit.alayer.exceptions.*;
import org.fruit.alayer.SUT;
import org.fruit.alayer.State;
import org.fruit.alayer.Verdict;
import org.fruit.monkey.Settings;
import nl.ou.testar.tgherkin.protocol.DocumentProtocol;

public class Protocol_testOU extends DocumentProtocol { // DefaultProtocol {

	/** 
	 * Called once during the life time of TESTAR.
	 * This method can be used to perform initial setup work
	 * @param   settings   the current TESTAR settings as specified by the user.
	 */
	@Override
	protected void initialize(Settings settings) {
		super.initialize(settings);
	}
	

	/**
	 * Begin sequence.
	 * @param system the SUT
	 * @param state the SUT's current state
	 * This method is invoked each time the TESTAR starts to generate a new sequence
	 */
	 @Override
	protected void beginSequence(SUT system, State state) {
		super.beginSequence(system, state);
	}
	

	/**
	 * This method is called when TESTAR starts the System Under Test (SUT). The method should
	 * take care of 
	 *   1) starting the SUT (you can use TESTAR's settings obtainable from <code>settings()</code> to find
	 *      out what executable to run)
	 *   2) bringing the system into a specific start state which is identical on each start (e.g. one has to delete or restore
	 *      the SUT's configuration files etc.)
	 *   3) waiting until the system is fully loaded and ready to be tested (with large systems, you might have to wait several
	 *      seconds until they have finished loading)
     * @return  a started SUT, ready to be tested.
	 */
	@Override
	protected SUT startSystem() throws SystemStartException {
        return super.startSystem();

	}



	/**
	 * This method is called when the TESTAR requests the state of the SUT.
	 * Here you can add additional information to the SUT's state or write your
	 * own state fetching routine. The state should have attached an oracle 
	 * (TagName: <code>Tags.OracleVerdict</code>) which describes whether the 
	 * state is erroneous and if so why.
	 * @return  the current state of the SUT with attached oracle.
	 */
	@Override
	protected State getState(SUT system) throws StateBuildException {
		return super.getState(system);

	}



	/**
	 * This is a helper method used by the default implementation of <code>buildState()</code>
	 * It examines the SUT's current state and returns an oracle verdict.
	 * @return oracle verdict, which determines whether the state is erroneous and why.
	 */
	@Override
	protected Verdict getVerdict(State state) {
		return super.getVerdict(state);
		
	}
	
	/**
	 * This method is used by TESTAR to determine the set of currently available actions.
	 * You can use the SUT's current state, analyze the widgets and their properties to create
	 * a set of sensible actions, such as: "Click every Button which is enabled" etc.
	 * The return value is supposed to be non-null. If the returned set is empty, TESTAR
	 * will stop generation of the current action and continue with the next one.
	 * @param system the SUT
	 * @param state the SUT's current state
	 * @return  a set of actions
	 */
	@Override
	protected Set<Action> deriveActions(SUT system, State state) throws ActionBuildException {
		return super.deriveActions(system,state);
	}

	/**
	 * Select one of the possible actions (e.g. at random)
	 * @param state the SUT's current state
	 * @param actions the set of available actions as computed by <code>buildActionsSet()</code>
	 * @return  the selected action (non-null!)
	 */
	@Override
	protected Action selectAction(State state, Set<Action> actions) { 
		return super.selectAction(state, actions);

	}


	/**
	 * Execute the selected action.
	 * @param system the SUT
	 * @param state the SUT's current state
	 * @param action the action to execute
	 * @return whether or not the execution succeeded
	 */
	@Override
	protected boolean executeAction(SUT system, State state, Action action) {
		return super.executeAction(system, state, action);
	}
	

	/**
	 * TESTAR uses this method to determine when to stop the generation of actions for the
	 * current sequence. You could stop the sequence's generation after a given amount of executed
	 * actions or after a specific time etc.
	 * @return  if <code>true</code> continue generation, else stop
	 */
	@Override
	protected boolean moreActions(State state) {
		return super.moreActions(state);

	}


	/** 
	 * This method is invoked each time after TESTAR finished the generation of a sequence.
	 */
	@Override
	protected void finishSequence(File recordedSequence) {
		super.finishSequence(recordedSequence);
	}


	/**
	 * TESTAR uses this method to determine when to stop the entire test.
	 * You could stop the test after a given amount of generated sequences or
	 * after a specific time etc.
	 * @return  if <code>true</code> continue test, else stop	 */
	@Override
	protected boolean moreSequences() {
		return super.moreSequences();
	}
	
}