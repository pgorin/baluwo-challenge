package com.baluwo.challenge.domain.model;

import static java.util.UUID.randomUUID;

public class Clients {

    public static final Client leoMessi = new Client(randomUUID(), new ClientInfo("Leo Messi"));
    public static final Client kunAguero = new Client(randomUUID(), new ClientInfo("Kun Aguero"));

}
