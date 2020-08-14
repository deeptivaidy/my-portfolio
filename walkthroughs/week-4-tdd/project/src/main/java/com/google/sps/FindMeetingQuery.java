// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public final class FindMeetingQuery {
  
  /**
   * Finds possible time ranges for a specific meeting so that all participants may attend
   * @param events All of the events for all possible people
   * @param request The specific meeting request containing information about participants and duration
   * @return A range of times for the meeting to occur
   */
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        //Checks if we have a valid meeting duration and attendees
        if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
            return Arrays.asList();
        } 
        if(request.getAttendees().isEmpty()) {
            return Arrays.asList(TimeRange.WHOLE_DAY);
        } 


        Collection<TimeRange> validTimes = new ArrayList<TimeRange>();
        Event[] attendeeEvents = getAttendeeEvents(events, request.getAttendees());
        
        int start = TimeRange.START_OF_DAY;
        int i = 0;

        //Iterates through the meeting attendees' events to find open time ranges
        while (i < attendeeEvents.length) {

            //If the current event occurs before our next possible start time, it means there 
            //are multiple events overlapping with a single large event, and we can skip this event
            if (attendeeEvents[i].getWhen().end() < start) {
                i++;
                continue;
            }
            
            //if the current event overlaps but does not pass the previous condition, then
            //out current event ends after "start", meaning we must update "start" and skip this event
            if(attendeeEvents[i].getWhen().contains(start)) {
                start = attendeeEvents[i].getWhen().end();
                i++;
                continue;
            }

            //Create a new time range the start to the beginning of next event
            TimeRange valid = TimeRange.fromStartEnd(start, attendeeEvents[i].getWhen().start(), false);

            if (valid.duration() >= request.getDuration()) {
                validTimes.add(valid);
            }
            
            //Checks if the next event exists and overlaps with the current event
            if ((i+1) < attendeeEvents.length && attendeeEvents[i].getWhen().overlaps(attendeeEvents[i+1].getWhen())) {
                //start will be set to whichever end is larger and will skip the next event
                start = (attendeeEvents[i].getWhen().end() > attendeeEvents[i+1].getWhen().end() ? attendeeEvents[i].getWhen().end() : attendeeEvents[i + 1].getWhen().end());
                i++;
            } else {
                start = attendeeEvents[i].getWhen().end();
            }
            i++;

        }
        if (TimeRange.END_OF_DAY - start >= request.getDuration()) {
            validTimes.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, true));
        }
        return validTimes;    

    }
  
    /**
     * Finds and orders the events for certain people based on start time
     * @param events All events for all possible people
     * @param attendees The attendees that we are trying to find events for
     * @return An array containing all of the attendees' events sorted in order of start time
     */
    private Event[] getAttendeeEvents(Collection<Event> events, Collection<String> attendees) {
        //Hashmap with TimeRange keys allows us to sort the keys by start time and thereby sort the events
        HashMap<TimeRange, Event> attendeeEvents = new HashMap<TimeRange, Event>();
        for (Event e : events) {
            //People in event e
            Collection <String> eventPeople = new ArrayList<String>(e.getAttendees());

            //If there is someone in event e who is also in our meeting, 
            //add to event list to consider while scheduling our meeting
            if(!Collections.disjoint(eventPeople, attendees)) {
                attendeeEvents.put(e.getWhen(), e);
            }
        }
        
        //Sorts the events based on start time
        ArrayList<TimeRange> sortedRanges= new ArrayList<TimeRange>(attendeeEvents.keySet());
        Collections.sort(sortedRanges, TimeRange.ORDER_BY_START);
        
        Event[] sortedArray = new Event[sortedRanges.size()];

        for (int i = 0; i < sortedArray.length; i++) {
            sortedArray[i] = attendeeEvents.get(sortedRanges.get(i));
        }
        return sortedArray;
    }
}
