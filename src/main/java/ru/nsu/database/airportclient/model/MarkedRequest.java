package ru.nsu.database.airportclient.model;

import ru.nsu.database.airportclient.model.requests.IRequest;

public class MarkedRequest {
    private final IRequest request;
    private final String rights;

    public MarkedRequest(IRequest request, String rights) {
        this.request = request;
        this.rights = rights;
    }

    public IRequest getRequest() {
        return request;
    }

    public String getRights() {
        return rights;
    }
}
