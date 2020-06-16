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
   * Query timeslots to find available time slots for attendees.
   * Optional attendees are also considered depending on several cases:
   * The basic functionality of optional attendees is that if one or more 
   * time slots exists so that both mandatory and optional attendees can attend, 
   * return those time slots. Otherwise, return the time slots that fit just 
   * the mandatory attendees. 
   *
   * Worst-case runtime: O(events * attendees).
  */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<String> mandatoryAttendees = request.getAttendees();
    Collection<String> allAttendees = new ArrayList<String>();
    allAttendees.addAll(mandatoryAttendees);
    allAttendees.addAll(request.getOptionalAttendees());
    
    // Available time list for all attendees, including optional attendees.
    List<TimeRange> allAttendeesAvailableTimes = getAvailableTimes(events, allAttendees, request.getDuration());

    // Edge case: No mandatory attendees or at least one available time slot for all attendees.
    if (mandatoryAttendees.isEmpty() || !allAttendeesAvailableTimes.isEmpty()) {
      return allAttendeesAvailableTimes;
    }

    // Edge case: Meeting duration exceeds a whole day.
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return Arrays.asList();
    }

    // Create a busy schedule and use it to create a list of available times.
    // If optional attendees is busy that creates 0 available time slots, ignore optional attendee(s).
    return getAvailableTimes(events, mandatoryAttendees, request.getDuration());
  }

  /** 
   * Create a list of timeslots where attendees are available based on meeting time duration.
   * Runtime: O(n) where n = size of busyTimes list.
  */
  public List<TimeRange> getAvailableTimes(Collection<Event> events, Collection<String> attendees, long meetingDuration) {
    List<TimeRange> busyTimes = getBusyTimes(events, attendees);
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
}
