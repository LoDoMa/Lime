
lime.include("genome")

local genome

local compos = {}
local joints = {}

local jointState = {}
local jointTime = {}

local NEXT = { p = "r", r = "w", w = "p" }

local function createBody()
    for i = 1, genome.bodyc do
        local size = genome.size[i]

        lime.startComponent()
        lime.setInitialPosition(0.0, 0.0)
        lime.setInitialAngle(0.0)
        lime.setComponentType("dynamic")
        lime.setShapeType("polygon")
        lime.setShapeVertices(0, 0, size.x, 0, size.x, size.y, 0, size.y)
        lime.setComponentDensity(1.0)
        lime.setComponentFriction(0.6)
        lime.setComponentRestitution(0.3)
        compos[i] = lime.endComponent()
    end
end

local function createJoints()
    for i = 2, genome.bodyc do
        local parent = genome.parent[i]
        local anchor = genome.anchor[i]
        local size = genome.size[i]
        local sizep = genome.size[parent]

        lime.startJoint()
        lime.setJointComponentA(compos[i])
        lime.setJointComponentB(compos[parent])
        lime.enableJointCollision(true)
        lime.setJointType("revolute")
        lime.setRevoluteAnchorA(anchor.x1 * size.x, anchor.y1 * size.y)
        lime.setRevoluteAnchorA(anchor.x2 * sizep.x, anchor.y2 * sizep.y)
        joints[i] = lime.endJoint()

        jointState[i] = "w"
        jointTime[i] = 0

        lime.selectJoint(joints[i])
        lime.enableRevoluteAngleLimit(false)
        lime.enableRevoluteMotor(true)
        lime.setRevoluteMaxMotorTorque(100000)
    end
end

function Lime_Init(entityID)
    genome = lime.getAndClearAttribute(entityID, "genome")
    genome_print(genome)

    createBody()
    createJoints()
end

function Lime_Update(timeDelta)
    for i = 2, genome.bodyc do
        lime.selectJoint(joints[i])
        lime.setRevoluteMotorSpeed(genome.stren[i][jointState[i]])

        jointTime[i] = jointTime[i] + timeDelta;
        if jointTime[i] >= genome.timing[i][jointState[i]] then
            jointTime[i] = jointTime[i] - genome.timing[i][jointState[i]]
            jointState[i] = NEXT[jointState[i]]
        end
    end
end

function Lime_Clean()
    for i = 1, genome.bodyc do
        if i > 1 then
            lime.removeJoint(joints[i])
        end
        lime.removeComponent(compos[i])
    end
end
