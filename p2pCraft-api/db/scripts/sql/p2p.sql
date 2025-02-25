
CREATE TABLE IF NOT EXISTS client (
    uuid UUID PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    password TEXT NOT NULL,
    active BOOLEAN DEFAULT true NOT NULL
);

CREATE TABLE IF NOT EXISTS client_friend (
    uuid UUID PRIMARY KEY NOT NULL,
    client_uuid UUID NOT NULL,
    friend_uuid UUID NOT NULL,
    FOREIGN KEY (client_uuid) REFERENCES client(uuid) ON DELETE CASCADE,
    FOREIGN KEY (friend_uuid) REFERENCES client(uuid) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS map_configuration (
    uuid UUID PRIMARY KEY NOT NULL,
    map_url TEXT NOT NULL,
    seed TEXT NOT NULL,
    version TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS server (
    uuid UUID PRIMARY KEY NOT NULL,
    map_config UUID NOT NULL,
    name TEXT NOT NULL,
    static_ip TEXT NOT NULL,
    last_volatile_ip TEXT,
    open BOOLEAN DEFAULT false,
    active BOOLEAN DEFAULT true,
    FOREIGN KEY (map_config) REFERENCES map_configuration(uuid) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS server_access (
    uuid UUID PRIMARY KEY NOT NULL,
    server_uuid UUID NOT NULL,
    client_uuid UUID NOT NULL,
    role TEXT NOT NULL,
    FOREIGN KEY (client_uuid) REFERENCES client(uuid) ON DELETE CASCADE,
    FOREIGN KEY (server_uuid) REFERENCES server(uuid) ON DELETE CASCADE
);