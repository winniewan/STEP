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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * FindMeetingQuery is the "find a meeting" feature class for when given the meeting
 * information, return the times when the meeting could happen that day.
*/
public final class FindMeetingQuery {

  /** 
   * Query timeslots where all attendees must be free to attend the meeting.
   * Worst-case runtime: O(events * attendees).
  */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<String> attendees = request.getAttendees();

    // Edge case: No attendees.
    if (attendees.isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    long meetingDuration = request.getDuration();

    // Edge case: Meeting duration exceeds a whole day.
    if (meetingDuration > TimeRange.WHOLE_DAY.duration()) {
      return Arrays.asList();
    }

    // Retrieve a schedule of when attendees are busy given events.
    List<TimeRange> busyTimes = getBusyTimes(events, attendees);

    // Use the busy schedule to create a list of available times.
    return getAvailableTimes(busyTimes, meetingDuration);
  }
  
  /** 
   * Create a sorted list of timeslots where attendees are busy. 
   * Worst-case runtime: O(events * attendees).
  */
  public List<TimeRange> getBusyTimes(Collection<Event> events, Collection<String> attendees) {
    List<TimeRange> busyTimes = new ArrayList<TimeRange>();
    
    // For each event, if an attendee(s) is attending, add it to busyTimes list.
    for(Event currEvent: events) {
      Set<String> currEventAttendees = currEvent.getAttendees();
      
      // Check if an attendee(s) is busy with current event.
      boolean isAttendeeBusy = false;
      for(String attendee: attendees) {
        if (currEventAttendees.contains(attendee)) {
          isAttendeeBusy = true;
          break;
        }
      }

      // If the attendee is busy, add it to busyTimes list.
      if(isAttendeeBusy) {
        busyTimes.add(currEvent.getWhen());
      }
    }

    Collections.sort(busyTimes, TimeRange.ORDER_BY_START);
    return busyTimes;
  }

  /** 
   * Create a list of timeslots where attendees are available based on meeting time duration.
   * Runtime: O(n) where n = size of busyTimes list.
  */
  public List<TimeRange> getAvailableTimes(List<TimeRange> busyTimes, long meetingDuration) {
    List<TimeRange> availableTimes = new ArrayList<TimeRange>();
      
    // Initialize the start time. 
    int startTime = TimeRange.START_OF_DAY;

    // Find non-busy times. 
    for (TimeRange busyTime: busyTimes) {
      int endTime = busyTime.start();
          
      // If there's a non-busy time slot, then check if meeting duration fits in time slot.
      if (startTime < endTime) {
        TimeRange timeslot = TimeRange.fromStartEnd(startTime, endTime, false);
              
        // If meeting duration fits into free timeslot, add it to availableTimes list.
        if (timeslot.duration() >= meetingDuration) {
          availableTimes.add(timeslot);
        }
      }

      // Update startTime.
      startTime = Math.max(startTime, busyTime.end());
    }

    // Add the rest of the day if there's any.
    if (TimeRange.END_OF_DAY - startTime >= meetingDuration) {
      availableTimes.add(TimeRange.fromStartEnd(startTime, TimeRange.END_OF_DAY, true));
    }

    return availableTimes;
  }
}
