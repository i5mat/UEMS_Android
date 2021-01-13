package my.edu.utem.uemsattendance;

public class Event {
    private String name;
    private String description;
    private String venue;
    private int capacity;
    private String start_event, end_event;

    public Event () {}

    public Event(String name, String description, String venue, int capacity, String start_event, String end_event) {
        this.name = name;
        this.description = description;
        this.venue = venue;
        this.capacity = capacity;
        this.start_event = start_event;
        this.end_event = end_event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStart_event() {
        return start_event;
    }

    public void setStart_event(String start_event) {
        this.start_event = start_event;
    }

    public String getEnd_event() {
        return end_event;
    }

    public void setEnd_event(String end_event) {
        this.end_event = end_event;
    }
}
