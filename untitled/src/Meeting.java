import java.util.*;

/**
 *       use Event to represent meeting start event and end event.
 *
 *       Sort them, the free time is between the lastMeeting
 *       and the next start Meeting
 */
public class Meeting {
    public List<Interval> getFreeTime(List<List<Interval>> schedule) {
        List<Interval> res = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        for (List<Interval> intervals : schedule) {
            for (Interval interval : intervals) {
                events.add(new Event(interval.start, 0));
                events.add(new Event(interval.end, 1));
            }
        }
        events.sort((a, b) -> {
            if (a.time == b.time) {
                return a.status - b.status;
            }
            return a.time - b.time;
        });

        int start = -1;
        int count = 0;
        for (Event event : events) {
            //if it's a start
            if (event.status == 0) {
                //free time ends
                if (count == 0 && start != -1) {
                    res.add(new Interval(start, event.time));
                }
                count++;
            } else {
                start = event.time;
                count--;
            }
        }
        return res;
    }

    //follow up: if we need find free time that >= k people are in rest

    public List<Interval> getFreeTimeII(List<List<Interval>> schedule, int k) {
        List<Interval> res = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        if (schedule.isEmpty()) {
            return res;
        }
        int totalEmployee = schedule.size();
        for (List<Interval> intervals : schedule) {
            for (Interval interval : intervals) {
                events.add(new Event(interval.start, 0));
                events.add(new Event(interval.end, 1));
            }
        }
        if (events.isEmpty()) {
            return res;
        }
        events.sort((a, b) -> {
            if (a.time == b.time) {
                return a.status - b.status;
            }
            return a.time - b.time;
        });

        //for k = totalEmployee, which means previous problem, our start is first time we don't have any meeting
        //but for other k smaller than it, our start time is first meeting's start time
        int start = -1;
        if (k < totalEmployee) {
            start = events.get(0).time;
        }
        int numAtWork = 0;
        for (Event event : events) {
            //if it's a start
            if (event.status == 0) {
                // if adding current meeting will break the free time condition,
                // we add previous recorded free time
                if (start != -1 && numAtWork == totalEmployee - k) {
                    res.add(new Interval(start, event.time));
                }
                numAtWork++;
            } else {
                numAtWork--;
                if (numAtWork == totalEmployee - k) {
                    start = event.time;
                }
            }
        }
        res.add(new Interval(start, events.get(events.size() - 1).time));
        return res;
    }



    static class Interval {
        int start;
        int end;
        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
    class Event {
        int time;
        //0 for start, 1 for end
        int status;

        public Event(int time, int status) {
            this.time = time;
            this.status = status;
        }
    }

    public static void main(String[] args) {
        List<List<Interval>> schedule = new ArrayList<>();
        List<Interval> p1 = new ArrayList<>();
        p1.add(new Interval(1, 3));
        p1.add(new Interval(4, 7));
        List<Interval> p2 = new ArrayList<>();
        p2.add(new Interval(2, 4));
        p2.add(new Interval(6, 8));
        List<Interval> p3 = new ArrayList<>();
        p3.add(new Interval(1, 2));
        List<Interval> p4 = new ArrayList<>();
        p4.add(new Interval(2, 8));
        schedule.add(p1);
        schedule.add(p2);
        schedule.add(p3);
        schedule.add(p4);
        Meeting meeting = new Meeting();
        List<Interval> res = meeting.getFreeTimeII(schedule, 2);
        for (Interval interval : res) {
            System.out.println("start : " + interval.start +"; end : " + interval.end);
        }

    }
}
