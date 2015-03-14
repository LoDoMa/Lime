
lime.include("genome")

local users = {}

local genome
local creature

function Lime_WorldInit()
    lime.setWorldGravity(0, -5)
end

local function onJoinListener(userID)
    users[userID] = true

    lime.setCameraTranslation(userID, -16, -9)
    lime.setCameraScale(userID, 32, 18)
end

local function onLeaveListener(userID)
    users[userID] = nil
end

function Lime_Init()
    lime.addEventListener("Lime::OnJoin", onJoinListener)
    lime.addEventListener("Lime::OnLeave", onLeaveListener)

    local light = lime.newLight()
    lime.setLightPosition(light, 0, 10)
    lime.setLightRadius(light, 40)
    lime.setLightColor(light, 1, 1, 1, 1)

    lime.startComponent()
    lime.setInitialPosition(-100.0, -5.0)
    lime.setInitialAngle(0.0)
    lime.setComponentType("static")
    lime.setShapeType("polygon")
    lime.setShapeVertices(0, 0, 200, 0, 200, 1, 0, 1)
    lime.setComponentDensity(1.0)
    lime.setComponentFriction(0.6)
    lime.setComponentRestitution(0.3)
    local floor = lime.endComponent()

    genome = genome_initial()
end

function Lime_Update(timeDelta)
    for k, v in pairs(users) do
        lime.setInputData(k)
        if lime.getKeyPress(lime.KEY_SPACE) or lime.getKeyPress(lime.KEY_R) then
            if creature then
                lime.removeEntity(creature)
            end
            
            if lime.getKeyPress(lime.KEY_SPACE) then
                genome_modify(genome)
            end

            genome_print(genome)

            creature = lime.newEntity()
            lime.setAttribute(creature, "genome", genome)
            lime.assignScript(creature, "gencreature")
        end
    end
end

function Lime_Clean()

end


