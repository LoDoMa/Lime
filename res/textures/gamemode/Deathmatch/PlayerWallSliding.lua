
root = {
    offset = { x = 0, y = 0, },
    children = {
        body = {
            offset = { x = 0, y = 0, },

            texture = "gamemode/Deathmatch/PlayerBody",
            textureSize = { x = 0.6, y = 0.5 },
            textureOffset = { x = 0, y = 0.25, },
            
            children = {
                armLeft = {
                    offset = { x = 0, y = 0.5, },

                    texture = "gamemode/Deathmatch/PlayerLegLeft",
                    textureSize = { x = -0.5, y = -0.5 },
                    textureOffset = { x = 0, y = 0.25, },
                },
                head = {
                    offset = { x = 0, y = 0.5, },

                    texture = "gamemode/Deathmatch/PlayerHairLeft",
                    textureSize = { x = 1.7, y = 1.7 },
                    textureOffset = { x = 0, y = 0.25, },

                    children = {
                        head2 = {
                            offset = { x = 0, y = 0, },

                            texture = "gamemode/Deathmatch/PlayerHeadLeft",
                            textureSize = { x = 1.7, y = 1.7 },
                            textureOffset = { x = 0, y = 0.25, },
                        },
                    },
                },
            },
            childrenBack = {
                armRight = {
                    offset = { x = 0, y = 0.5, },

                    texture = "gamemode/Deathmatch/PlayerLegLeft",
                    textureSize = { x = -0.5, y = -0.5 },
                    textureOffset = { x = 0, y = 0.25, },
                },
            },
        },
        legLeft = {
            offset = { x = 0, y = 0, },

            texture = "gamemode/Deathmatch/PlayerLegLeft",
            textureSize = { x = -0.5, y = -0.5 },
            textureOffset = { x = 0, y = 0.2, },
        },
    },

    childrenBack = {
        legRight = {
            offset = { x = 0, y = 0, },

            texture = "gamemode/Deathmatch/PlayerLegLeft",
            textureSize = { x = -0.5, y = -0.5 },
            textureOffset = { x = 0, y = 0.2, },
        },
    },
}

animation = {
    duration = 1,
    keyframes = {
        { body = root.children.body.children.head, time = 0.0, angle = 47 },
        { body = root.children.body.children.head, time = 0.5, angle = 43 },
        { body = root.children.body.children.armLeft, time = 0.0, angle = 280 },
        { body = root.children.body.childrenBack.armRight, time = 0.0, angle = 120 },
        { body = root.children.legLeft, time = 0.0, angle = 240 },
        { body = root.childrenBack.legRight, time = 0.0, angle = 170 },
    },
}
