package sockets.dependencies;

import sockets.Connection;

public interface ConnectionListener {

    void serverLogEvent(String log);
    void connectionLogEvent(Connection connection, String msg);

    void serverOff();

    void connectionCreatedEvent(Connection connection);
    void connectionClosedEvent();

}
