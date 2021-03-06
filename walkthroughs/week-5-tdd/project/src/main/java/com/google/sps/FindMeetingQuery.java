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
import java.util.LinkedList;
import java.util.Collection;

public final class FindMeetingQuery {

    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        // has one slot for every minute of a day
        // The minute is true if all attendees are available in that minute, false
        // otherwise
        boolean[] availabilityWithOptional = new boolean[TimeRange.END_OF_DAY + 1];
        boolean[] availabilityMandatoryOnly = new boolean[TimeRange.END_OF_DAY + 1];

        Arrays.fill(availabilityWithOptional, true);
        Arrays.fill(availabilityMandatoryOnly, true);

        for (Event event : events) {
            boolean isRelevantMandatoryAttendees = isEventRelevant(event, request.getAttendees());
            boolean isRelevantOptionalAttendees = isEventRelevant(event, request.getOptionalAttendees());

            if (isRelevantMandatoryAttendees) {
                recordBusyTime(event.getWhen(), availabilityMandatoryOnly);
                recordBusyTime(event.getWhen(), availabilityWithOptional);
            }

            if (isRelevantOptionalAttendees) {
                recordBusyTime(event.getWhen(), availabilityWithOptional);
            }
        }

        Collection<TimeRange> availableTimesWithOptionalAttendees = getAvailableTimes(availabilityWithOptional,
                request);

        if (availableTimesWithOptionalAttendees.size() > 0) {
            return availableTimesWithOptionalAttendees;
        }

        Collection<TimeRange> availableTimesMandatoryOnly = getAvailableTimes(availabilityMandatoryOnly, request);

        return availableTimesMandatoryOnly;

    }

    private Collection<TimeRange> getAvailableTimes(boolean[] availability, MeetingRequest request) {
        Collection<TimeRange> availableTimes = new LinkedList<TimeRange>();

        int startIndex = TimeRange.START_OF_DAY;

        while (startIndex <= TimeRange.END_OF_DAY) {
            if (!availability[startIndex]) {
                startIndex++;
                continue;
            }

            int endIndex = startIndex;

            while (endIndex <= TimeRange.END_OF_DAY) {
                if (!availability[endIndex]) {
                    break;
                }
                endIndex++;
            }

            if (endIndex - startIndex >= request.getDuration()) {
                TimeRange availableTime = TimeRange.fromStartEnd(startIndex, endIndex, false);
                availableTimes.add(availableTime);
            }

            startIndex = endIndex;
        }

        return availableTimes;
    }

    /*
     * Determines if the event is relevant for at least one of the attendees. It is
     * relevant if the attendee is attending the event
     **/
    private Boolean isEventRelevant(Event event, Collection<String> requiredAttendees) {
        for (String attendee : event.getAttendees()) {
            if (requiredAttendees.contains(attendee)) {
                return true;
            }
        }
        return false;
    }

    private void recordBusyTime(TimeRange newTime, boolean[] availability) {
        for (int i = newTime.start(); i < newTime.end(); i++) {
            availability[i] = false;
        }
    }

}
