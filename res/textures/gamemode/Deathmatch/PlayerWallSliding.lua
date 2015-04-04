
root = {
    length = 0,
    width = 0,
    children = {
        body = {
            length = 0.5,
            width = 0.6,
            children = {
                head = {
                    length = 1.5,
                    width = 1.5,
                },
                armLeft = {
                    length = 0.5,
                    width = 0.3,
                },
                armRight = {
                    length = 0.5,
                    width = 0.3,
                },
            },
        },
        legLeft = {
            length = 0.5,
            width = 0.3,
        },
        legRight = {
            length = 0.5,
            width = 0.3,
        },
    },
}

animation = {
    duration = 1,
    keyframes = {
        { body = root.children.body.children.head, time = 0.0, angle = 47 },
        { body = root.children.body.children.head, time = 0.5, angle = 43 },
        { body = root.children.body.children.armLeft, time = 0.0, angle = 280 },
        { body = root.children.body.children.armRight, time = 0.0, angle = 120 },
        { body = root.children.legLeft, time = 0.0, angle = 240 },
        { body = root.children.legRight, time = 0.0, angle = 170 },
    },
}
