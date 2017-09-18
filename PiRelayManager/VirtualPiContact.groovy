/**
 *  Pi Relay Control
 *
 *  Copyright 2016 Tom Beech
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
 
metadata {
	definition (name: "Virtual Pi Contact", namespace: "Kalltech", author: "ibeech") {
		capability "Contact Sensor"
        capability "Refresh"
		capability "Polling"
        
        command "changeState", ["string"]
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {    
        
		 standardTile("contact", "device.contact", width: 1, height: 1, canChangeIcon: true) {
			state "closed", label:'${name}', action:"contact.open", icon:"st.contactes.contact.closed", backgroundColor:"#79b821"
			state "open", label:'${name}', action:"contact.closed", icon:"st.contactes.contact.open", backgroundColor:"#ffffff"
		}
        
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
			state("default", label:'refresh', action:"polling.poll", icon:"st.secondary.refresh-icon")
		}

		main "contact"
		details (["contact", "refresh"])
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Virtual contact parsing '${description}'"
}

def poll() {
	log.debug "Executing 'poll'"   
        
        def lastState = device.currentValue("contact")
    	sendEvent(name: "contact", value: device.deviceNetworkId + ".refresh")
        sendEvent(name: "contact", value: lastState);
}

def refresh() {
	log.debug "Executing 'refresh'"
    
	poll();
}

def closed() {
	log.debug "Executing 'closed'"	     
    
    sendEvent(name: "contact", value: device.deviceNetworkId + ".closed");    
    sendEvent(name: "contact", value: "closed");    
}

def open() {
	log.debug "Executing 'open'"
	    
	sendEvent(name: "contact", value: device.deviceNetworkId + ".open");     
    sendEvent(name: "contact", value: "open");
}

def changeState(newState) {

	log.trace "Received update that this contact is now $newState"
	switch(newState) {
    	case 1:
			sendEvent(name: "contact", value: "closed")
            break;
    	case 0:
        	sendEvent(name: "contact", value: "open")
            break;
    }
}
