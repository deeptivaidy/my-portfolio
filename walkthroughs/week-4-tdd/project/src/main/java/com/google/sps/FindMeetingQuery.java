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
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    //Checks if we have a valid meeting duration and attendees
    if (request.getDuration() > (24*60)) {
        return Arrays.asList();
    } else if(request.getAttendees().isEmpty()) {
        return Arrays.asList(TimeRange.WHOLE_DAY);
    } else {
        // throw new UnsupportedOperationException("TODO: Implement this method.");
        Collection<TimeRange> validTimes = new ArrayList<TimeRange>();
        Collection<Event> attendeeEvents = getAttendeeEvents(events, request.getAttendees());
        
        int start = TimeRange.START_OF_DAY;
        Event current;
        while (!attendeeEvents.isEmpty()) {
            current = attendeeEvents.remove();
            validTimes.add(TimeRange.fromStartEnd(start, current.start(), false));

        }
        return validTimes;
        }

    }
  
    private Event[] getAttendeeEvents(Collection<Event> events, Collection<String> attendees) {
        //Hashmap with TimeRange keys allows us to sort the keys by start time and thereby sort the events
        HashMap<TimeRange, Event> aEvents = new HashMap<TimeRange, Event>();
        for (Event e : events) {
            //People in event e
            Collection <String> ePeople = new ArrayList<String>(e.getAttendees());

            //The attendees of our meeting that are in event e as well
            ePeople.retainAll(attendees);

            //If there is someone in event e who is also in our meeting, 
            //add to event list to consider while scheduling our meeting
            if(!ePeople.isEmpty()) {
                aEvents.put(e.getWhen(), e);
            }
        }
        
        ArrayList<TimeRange> sortedRanges= new ArrayList<TimeRange>(aEvents.keySet());
        //  for(TimeRange range : sortedRanges) {
        //     System.out.println(range);
        // }
        // System.out.println("\n");
        Collections.sort(sortedRanges, TimeRange.ORDER_BY_START);
        ArrayList <Event> sortedEvents = new ArrayList<Event>();
        
        for(TimeRange range : sortedRanges) {
            // System.out.println(range);
            sortedEvents.add(aEvents.get(range));
        }
        System.out.println("_____________________________________");
        return sortedEvents.toArray();
    }
}
