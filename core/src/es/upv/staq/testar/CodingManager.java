/***************************************************************************************************
 *
 * Copyright (c) 2013, 2014, 2015, 2016, 2017, 2018, 2019 Universitat Politecnica de Valencia - www.upv.es
 * Copyright (c) 2018, 2019 Open Universiteit - www.ou.nl
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************************************/


package es.upv.staq.testar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.zip.CRC32;

import org.fruit.alayer.Action;
import org.fruit.alayer.Role;
import org.fruit.alayer.State;
import org.fruit.alayer.Tag;
import org.fruit.alayer.Tags;
import org.fruit.alayer.Widget;
import org.fruit.alayer.actions.ActionRoles;

/**
 * Core coding manager.
 *
 */
public class CodingManager {

	private CodingManager() {}

	public static final int ID_LENTGH = 24; // 2 (prefixes) + 7 (MAX_RADIX) + 5 (max expected text length) + 10 (CRC32)

	// Identifier used by Widgets (included States) and Actions
	public static final String CONCRETE_ID = "ConcreteID";
	public static final String ABSTRACT_ID = "AbstractID";

	//Different identifiers used to Filter widgets into the Filter Protocol
	public static final String FILTER_R = "Filter_R";
	public static final String FILTER_R_T = "Filter_R_T";
	public static final String FILTER_R_T_P = "Filter_R_T_P";

	//Widgets properties used to Filter widgets
	private static final Tag<?>[] TAGS_FILTER_R = new Tag<?>[]{Tags.Role};
	private static final Tag<?>[] TAGS_FILTER_R_T = new Tag<?>[]{Tags.Role,Tags.Title};
	private static final Tag<?>[] TAGS_FILTER_R_T_P = new Tag<?>[]{Tags.Role,Tags.Title,Tags.Path};

	//Prefix identifiers used to construct the Filter Widget
	private static final String PREFIX_FILTER_R = "R";	
	private static final String PREFIX_FILTER_R_T = "T";	
	private static final String PREFIX_FILTER_R_T_P = "P";

	public static final String ID_PREFIX_CONCRETE = "C";
	public static final String ID_PREFIX_ABSTRACT = "A";

	public static final String ID_PREFIX_STATE = "S";
	public static final String ID_PREFIX_WIDGET = "W";
	public static final String ID_PREFIX_ACTION = "A";

	private static final Tag<?>[] DEFAULT_TAGS_CONCRETE_ID = new Tag<?>[]{Tags.Role,Tags.Title,Tags.Enabled, Tags.Path};
	private static final Tag<?>[] DEFAULT_TAGS_ABSTRACT_ID = new Tag<?>[]{Tags.Role};

	private static final Tag<?>[] DEFAULT_TAGS_ACTION_CONCRETE_ID = new Tag<?>[]{Tags.TargetConcreteID,Tags.Desc};
	private static final Tag<?>[] DEFAULT_TAGS_ACTION_ABSTRACT_ID = new Tag<?>[]{Tags.TargetAbstractID,Tags.Role};

	// two arrays to hold the tags that will be used in constructing the concrete and abstract for Widgets and State id's
	private static Tag<?>[] customTagsForConcreteId = DEFAULT_TAGS_CONCRETE_ID;
	private static Tag<?>[] customTagsForAbstractId = DEFAULT_TAGS_ABSTRACT_ID;

	// two arrays to hold the tags that will be used in constructing the concrete and abstract for Actions id's
	private static Tag<?>[] customTagsForActionConcreteId = DEFAULT_TAGS_ACTION_CONCRETE_ID;
	private static Tag<?>[] customTagsForActionAbstractId = DEFAULT_TAGS_ACTION_ABSTRACT_ID;

	/**
	 * Set the array of tags that should be used in constructing the concrete state id's.
	 *
	 * @param tags array
	 */
	public static synchronized void setCustomTagsForConcreteId(Tag<?>[] tags) {
		customTagsForConcreteId = tags;
	}

	/**
	 * Set the array of tags that should be used in constructing the abstract state id's.
	 *
	 * @param tags
	 */
	public static synchronized void setCustomTagsForAbstractId(Tag<?>[] tags) {
		customTagsForAbstractId = tags;
	}

	/**
	 * Returns the tags that are currently being used to create a custom abstract state id
	 * @return
	 */
	public static Tag<?>[] getCustomTagsForAbstractId() {
		return customTagsForAbstractId;
	}

	/**
	 * Set the array of tags that should be used in constructing the concrete Action id's.
	 *
	 * @param tags array
	 */
	public static synchronized void setCustomTagsForActionConcreteId(Tag<?>[] tags) {
		customTagsForActionConcreteId = tags;
	}

	/**
	 * Set the array of tags that should be used in constructing the abstract Action id's.
	 *
	 * @param tags
	 */
	public static synchronized void setCustomTagsForActionAbstractId(Tag<?>[] tags) {
		customTagsForActionAbstractId = tags;
	}

	// this map holds the state tags that should be provided to the coding manager
	// for use in constructing concrete and abstract state id's
	public static HashMap<String, Tag<?>> allowedStateTags = new HashMap<String, Tag<?>>() {
		{
			put("Role", Tags.Role);
			put("Title", Tags.Title);
			put("Path", Tags.Path);
			put("Enabled", Tags.Enabled);
		}
	};

	// this map holds the Action tags that should be provided to the coding manager
	// for use in constructing concrete and abstract Action id's
	public static HashMap<String, Tag<?>> allowedActionsTags = new HashMap<String, Tag<?>>() {
		{
			put("TargetAbstractID", Tags.TargetAbstractID);
			put("TargetConcreteID", Tags.TargetConcreteID);
			put("Desc", Tags.Desc);
			put("Role", Tags.Role);
		}
	};

	// ###########################################
	//  Widgets/States and Actions IDs management
	// ###########################################

	/**
	 * Builds IDs for a widget or state.
	 * @param widget A widget or a State (widget-tree, or widget with children)
	 * 
	 * An identifier (alphanumeric) for a state is built as: f(w1 + ... + wn),
	 * where wi (i=1..n) is the identifier for a widget in the widget-tree
	 * and the + operator is the concatenation of identifiers (alphanumeric).
	 * The order of the widgets in f is determined by the UI structure.
	 * f is a formula that converts, with low collision, a text of varying length
	 * to a shorter representation: hashcode(text) + length(text) + crc32(text).
	 * 
	 * An identifier (alphanumeric) for a widget is calculated based on
	 * the concatenation of a set of accessibility properties (e.g. ROLE, TITLE, ENABLED and PATH).
	 * An example for an enabled "ok" button could be: Buttonoktrue0,0,1 ("0,0,1" being the path in the widget-tree).
	 *
	 */
	public static synchronized void buildIDs(Widget widget){
		if (widget.parent() != null){
			widget.set(Tags.ConcreteID, ID_PREFIX_WIDGET + ID_PREFIX_CONCRETE + CodingManager.codify(widget, false, customTagsForConcreteId));
			widget.set(Tags.AbstractID, ID_PREFIX_WIDGET + ID_PREFIX_ABSTRACT + CodingManager.codify(widget, false, customTagsForAbstractId));
			widget.set(Tags.Filter_R, ID_PREFIX_WIDGET + PREFIX_FILTER_R + CodingManager.codify(widget, false, CodingManager.TAGS_FILTER_R));
			widget.set(Tags.Filter_R_T, ID_PREFIX_WIDGET + PREFIX_FILTER_R_T + CodingManager.codify(widget, false, CodingManager.TAGS_FILTER_R_T));
			widget.set(Tags.Filter_R_T_P, ID_PREFIX_WIDGET + PREFIX_FILTER_R_T_P + CodingManager.codify(widget, false, CodingManager.TAGS_FILTER_R_T_P));
		} else if (widget instanceof State) { // UI root
			String cid, aid, filter_R, filter_R_T, filter_R_T_P;
			cid = aid = filter_R = filter_R_T = filter_R_T_P = "";
			for (Widget w : (State) widget){
				if (w != widget){
					buildIDs(w);
					cid += w.get(Tags.ConcreteID);
					aid += w.get(Tags.AbstractID);
					filter_R += w.get(Tags.Filter_R);
					filter_R_T += w.get(Tags.Filter_R_T);
					filter_R_T_P += w.get(Tags.Filter_R_T_P);
				}
			}
			widget.set(Tags.ConcreteID, ID_PREFIX_STATE + ID_PREFIX_CONCRETE + CodingManager.toID(cid));
			widget.set(Tags.AbstractID, ID_PREFIX_STATE + ID_PREFIX_ABSTRACT + CodingManager.toID(aid));
			widget.set(Tags.Filter_R, ID_PREFIX_STATE + PREFIX_FILTER_R + CodingManager.toID(filter_R));
			widget.set(Tags.Filter_R_T, ID_PREFIX_STATE + PREFIX_FILTER_R_T + CodingManager.toID(filter_R_T));
			widget.set(Tags.Filter_R_T_P, ID_PREFIX_STATE + PREFIX_FILTER_R_T_P + CodingManager.toID(filter_R_T_P));
		}	
	}

	/**
	 * Builds IDs (abstract, concrete) for a set of actions.
	 * @param state Current State of the SUT
	 * @param actions The actions.
	 */
	public static synchronized void buildIDs(State state, Set<Action> actions){
		for (Action a : actions)
			CodingManager.buildIDs(state,a);
	}

	/**
	 * Builds IDs (abstract, concrete, precise) for an action.
	 * @param action An action.
	 */
	public static synchronized void buildIDs(State state, Action action){
		action.set(Tags.ConcreteID, ID_PREFIX_ACTION + ID_PREFIX_CONCRETE +
				CodingManager.codify(state.get(Tags.ConcreteID), action, customTagsForActionConcreteId));

		action.set(Tags.AbstractID, ID_PREFIX_ACTION + ID_PREFIX_ABSTRACT +
				CodingManager.codify(state.get(Tags.AbstractID), action, customTagsForActionAbstractId));
	}

	// ###############
	//  STATES CODING
	// ###############

	private static String codify(Widget state, boolean codifyContext, Tag<?>... tags){
		return toID(getWidgetString(state,codifyContext,tags));
	}

	private static String getWidgetString(Widget widget, boolean codifyContext, Tag<?>... tags){
		String ws = getTaggedString(widget,tags);
		if (codifyContext)
			ws += "#" + getWidgetContextString(widget);
		return ws;
	}

	private static String getTaggedString(Widget leaf, Tag<?>... tags){
		StringBuilder sb = new StringBuilder();
		for(Tag<?> t : tags)
			sb.append(leaf.get(t, null));
		return sb.toString();
	}

	private static String getWidgetContextString(Widget widget){
		return "";
	}

	// ################
	//  ACTIONS CODING
	// ################

	private static String codify(String stateID, Action action, Tag<?>... tags) {
		return toID(stateID + getActionString(action , tags));
	}

	private static String getActionString(Action action, Tag<?>... tags){
		return getTaggedString(action,tags);
	}

	private static String getTaggedString(Action leaf, Tag<?>... tags){
		StringBuilder sb = new StringBuilder();
		for(Tag<?> t : tags)
			sb.append(leaf.get(t, null));
		return sb.toString();
	}

	// ############
	//  IDS CODING
	// ############

	private static String lowCollisionID(String text){ // reduce ID collision probability
		CRC32 crc32 = new CRC32(); crc32.update(text.getBytes());
		return Integer.toUnsignedString(text.hashCode(), Character.MAX_RADIX) +
				Integer.toHexString(text.length()) +
				crc32.getValue();
	}

	private static String toID(String text){
		return lowCollisionID(text);
	}

	// #####################################
	// ## New abstract state model coding ##
	// #####################################

	/**
	 * This method will return the unique hash to identify the abstract state model
	 * @return String A unique hash
	 */
	public static String getAbstractStateModelHash(String applicationName, String applicationVersion) {
		// we calculate the hash using the tags that are used in constructing the custom abstract state id
		// for now, an easy way is to order them alphabetically by name
		Tag<?>[] abstractTags = getCustomTagsForAbstractId().clone();
		Arrays.sort(abstractTags, (Tag<?> tagA, Tag<?> tagB) -> {
			return tagA.name().compareTo(tagB.name());
		});
		StringBuilder hashInput = new StringBuilder();
		for (Tag<?> tag : abstractTags) {
			hashInput.append(tag.name());
		}
		// we add the application name and version to the hash input
		hashInput.append(applicationName);
		hashInput.append(applicationVersion);
		return lowCollisionID(hashInput.toString());
	}

	// #################
	//  Utility methods
	// #################

	public static Widget find(State state, String widgetID, String idType){
		Tag<String> t = null;
		switch(idType){
		case CodingManager.CONCRETE_ID:
			t = Tags.ConcreteID;
			break;
		case CodingManager.ABSTRACT_ID:
			t = Tags.AbstractID;
			break;
		}

		for (Widget w : state){
			if (widgetID.equals(w.get(t)))
				return w;
		}
		return null; // not found
	}



}
