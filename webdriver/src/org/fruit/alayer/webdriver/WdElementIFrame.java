/**
 * Copyright (c) 2019 Open Universiteit - www.ou.nl
 * Copyright (c) 2019 Universitat Politecnica de Valencia - www.upv.es
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
 *
 */


package org.fruit.alayer.webdriver;

import java.util.List;
import java.util.Map;

import org.fruit.alayer.Rect;

public class WdElementIFrame extends WdElement{

	private static final long serialVersionUID = 4401199406008503291L;
	
	public WdElementIFrame(Map<String, Object> packedElement, WdRootElement root, WdElement parent) {
		super(packedElement, root, parent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void fillRect(Map<String, Object> packedElement) {
		this.iframeContainer = this.parent.iframeContainer;
	    List<Long> rect = (List<Long>) packedElement.get("rect");
	    this.rect = Rect.from(rect.get(0) + this.iframeContainer.x(), rect.get(1) + this.iframeContainer.y(),
	    		rect.get(2), rect.get(3));
	  }
	
	@SuppressWarnings("unchecked")
	@Override
	protected void childElementTree(Map<String, Object> packedElement) {
		List<Map<String, Object>> wrappedChildren =
				(List<Map<String, Object>>) packedElement.get("wrappedChildren");
		for (Map<String, Object> wrappedChild : wrappedChildren) {
			WdElement child = new WdElementIFrame(wrappedChild, root, this);
			if (!Constants.hiddenTags.contains(child.tagName) &&
					!Constants.ignoredTags.contains(child.tagName)) {
				children.add(child);

			} 
		}

	}

}
