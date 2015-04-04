
root = {
    length = 0,
    width = 0,
    children = {
        body = {
            length = 0.35,
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
            length = 0.45,
            width = 0.3,
        },
        legRight = {
            length = 0.45,
            width = 0.3,
        },
    },
}

animation = {
    duration = 0.5,
    keyframes = {
        { body = root.children.body.children.head, time = 0.0, angle = 2 },
        { body = root.children.body.children.head, time = 0.25, angle = -2 },
        { body = root.children.body.children.armLeft, time = 0.0, angle = 290 },
        { body = root.children.body.children.armLeft, time = 0.25, angle = 220 },
        { body = root.children.body.children.armRight, time = 0.0, angle = 70 },
        { body = root.children.body.children.armRight, time = 0.25, angle = 140 },
        { body = root.children.legLeft, time = 0.0, angle = 230 },
        { body = root.children.legLeft, time = 0.25, angle = 190 },
        { body = root.children.legRight, time = 0.0, angle = 110 },
        { body = root.children.legRight, time = 0.25, angle = 170 },
    },
}
