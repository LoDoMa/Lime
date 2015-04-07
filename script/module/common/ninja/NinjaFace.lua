
local ninja
local ninjaFace
local ninjaMoves

local rootX
local rootY
local scaleX
local scaleY

-- Selections
local stillSelection
local walkingSelection
local fallingSelection
local fallingLeftSelection
local wallSlidingSelection

local function setNinja(ninja_, ninjaFace_, ninjaMoves_)
    ninja = ninja_
    ninjaFace = ninjaFace_
    ninjaMoves = ninjaMoves_
end

local function setRoot(rootX_, rootY_)
    rootX = rootX_
    rootY = rootY_
end

local function setScale(scaleX_, scaleY_)
    scaleX = scaleX_
    scaleY = scaleY_
end

local function setSelections(stillSelection_, walkingSelection_, fallingSelection_, fallingLeftSelection_, wallSlidingSelection_)
    stillSelection = stillSelection_
    walkingSelection = walkingSelection_
    fallingSelection = fallingSelection_
    fallingLeftSelection = fallingLeftSelection_
    wallSlidingSelection = wallSlidingSelection_
end

local function animate()
    local hasGround = ninjaMoves.getSensorData()
    local isMoving, isWallSliding, isRising, movementDirection = ninjaMoves.getState()

    lime.selectComponent(ninja)
    lime.selectShape(ninjaFace)
    lime.setShapeAnimationRoot(rootX, rootY)
    lime.setShapeAnimationScale(scaleX * -movementDirection, scaleY)
    
    local velocityX, velocityY = lime.getLinearVelocity()

    local selection = stillSelection
    if hasGround then
        if not isMoving then
            selection = stillSelection
        else
            selection = walkingSelection
        end
    elseif wallSliding then
        selection = wallSlidingSelection
    else
        if not isMoving then
            if isRising then
                local velocityX = lime.getLinearVelocity()
                if velocityX < -0.75 or velocityX > 0.75 then
                    selection = fallingLeftSelection
                else
                    selection = fallingSelection
                end
            else
                selection = fallingSelection
            end
        else
            selection = fallingLeftSelection
        end
    end

    lime.setShapeAnimationSelection(selection)
    lime.updateShape()
end

local moduleTable = {
    setNinja = setNinja,
    setRoot = setRoot,
    setScale = setScale,
    setSelections = setSelections,

    animate = animate,
}

-- The module table acts as a proxy
__LIME_MODULE_TABLE__ = {}

setmetatable(__LIME_MODULE_TABLE__, {
    __index = moduleTable,
    __newindex = function (t, k, v)
        error("attempt to modify a read-only table")
    end
})
