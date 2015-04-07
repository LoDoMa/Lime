
-- User data
local ninjaID
local ninjaUserID

-- Abilities
local abilityMovement = true
local abilityJump = true
local abilityWallJump = true
local abilityWallSliding = true

-- Sensor data
local hasGround = false
local hasWall = false
local hasWallLeft = false
local hasWallRight = false

-- Physics data
local movementVelocityX = 0
local groundAcceleration = 0
local airAcceleration = 0
local jumpImpulseY = 0
local wallJumpImpulseX = 0
local wallJumpMultiplierY = 0
local wallSlideMultiplierY = 0

-- State
local isMoving = false
local isWallSliding = false
local isRising = false
local movementDirection = 1
local movementDuration = 0

local function setUserData(ninjaID_, ninjaUserID_)
    ninjaID = ninjaID_
    ninjaUserID = ninjaUserID_
end

local function setAbilities(abilityMovement_, abilityJump_, abilityWallJump_, abilityWallSliding_)
    abilityMovement = abilityMovement_
    abilityJump = abilityJump_
    abilityWallJump = abilityWallJump_
    abilityWallSliding = abilityWallSliding_
end

local function setSensorData(hasGround_, hasWallLeft_, hasWallRight_)
    hasGround = hasGround_
    hasWall = hasWallLeft_ or hasWallRight_
    hasWallLeft = hasWallLeft_
    hasWallRight = hasWallRight_
end

local function setPhysicsData(movementVelocityX_, groundAcceleration_, airAcceleration_,
                              jumpImpulseY_, wallJumpImpulseX_, wallJumpMultiplierY_, wallSlideMultiplierY_)
    movementVelocityX = movementVelocityX_
    groundAcceleration = groundAcceleration_
    airAcceleration = airAcceleration_
    jumpImpulseY = jumpImpulseY_
    wallJumpImpulseX = wallJumpImpulseX_
    wallJumpMultiplierY = wallJumpMultiplierY_
    wallSlideMultiplierY = wallSlideMultiplierY_
end

local function getState()
    return isMoving, isWallSliding, isRising, movementDirection, movementDuration
end

local function getSensorData()
    return hasGround, hasWallLeft, hasWallRight
end

local function move(timeDelta)

    lime.setInputData(ninjaUserID)
    lime.selectComponent(ninjaID)

    local velocityX, velocityY = lime.getLinearVelocity()

    -- ----------------
    -- Code for jumping
    -- ----------------

    if abilityJump then
        if lime.getKeyPress(lime.KEY_W) or lime.getKeyPress(lime.KEY_UP) then
            local jumpDir = -2

            -- If we have ground, or walls on both sides, jump straight up
            if hasGround or (hasWallLeft and hasWallRight) then
                jumpDir = 0
            elseif abilityWallJump then
                -- If we're allowed to jump and have wall left, jump right
                -- Otherwise, if we have wall right, jump left
                if hasWallLeft then
                    jumpDir = 1
                elseif hasWallRight then
                    jumpDir = -1
                end
            end

            if jumpDir ~= -2 then
                local verticalJump = jumpImpulseY
                local horizontalJump = 0.0

                -- If we aren't jumping straight up, add X force and dampen Y force
                if jumpDir ~= 0 then
                    verticalJump = verticalJump * wallJumpMultiplierY
                    horizontalJump = wallJumpImpulseX * jumpDir
                end
                
                lime.applyLinearImpulseToCenter(horizontalJump, verticalJump)
            end
        end
    end

    -- ------------------------------------
    -- Code for walking and movement states
    -- ------------------------------------

    -- Target velocity is the final velocity we want to reach while moving

    local targetVelocity = 0

    -- First determine if we're moving at all

    isMoving = false
    if abilityMovement then
        if lime.getKeyState(lime.KEY_A) or lime.getKeyState(lime.KEY_LEFT) then
            isMoving = true
            movementDirection = -1
        elseif lime.getKeyState(lime.KEY_D) or lime.getKeyState(lime.KEY_RIGHT) then
            isMoving = true
            movementDirection = 1
        end
    end

    -- If we're moving, change the target velocity and update movementDuration
    -- Otherwise, set movementDuration to 0 (movementDuration is how long we've been moving for)

    if isMoving then
        targetVelocity = movementVelocityX * movementDirection
        movementDuration = movementDuration + timeDelta
    else
        movementDuration = 0
    end

    -- We'll apply force to correct the X velocity
    -- If on ground, multiply it by ground acceleration, otherwise by air acceleration

    local velocityX, velocityY = lime.getLinearVelocity()

    local inputForce = targetVelocity - velocityX
    if hasGround then
        inputForce = inputForce * groundAcceleration
    else
        inputForce = inputForce * airAcceleration
    end

    -- Apply the inputForce and some additional Y force, because it makes jumping look nicer
    -- NOTE: additional Y force must not be too high, because the player
    --       starts to jitter at that point

    lime.applyForceToCenter(inputForce, 0.0)

    -- We are rising if we aren't touching the ground and our Y velocity is greater than 0
    -- The "not hasGround" check is important because Y velocity can be greater that 0
    -- even while standing on flat ground because of JBox2D position corrections.

    local velocityX, velocityY = lime.getLinearVelocity()

    isRising = not hasGround and velocityY > 0

    -- Check if we are wall sliding
    -- We are wall sliding if we have that ability, don't have ground, are near a wall, and aren't rising
    -- If we are wall sliding, dampen the Y velocity a bit

    wallSliding = abilityWallSliding and not hasGround and hasWall and not isRising
    if wallSliding then
        lime.setLinearVelocity(velocityX, velocityY - velocityY * timeDelta * wallSlideMultiplierY)
    end

    -- After all that moving, change the movementDirection state to the direction of the velocity

    if velocityX < -0.75 then
        movementDirection = -1
    elseif velocityX > 0.75 then
        movementDirection = 1
    end

end

local moduleTable = {
    setUserData = setUserData,
    setAbilities = setAbilities,
    setSensorData = setSensorData,
    setPhysicsData = setPhysicsData,
    getState = getState,
    getSensorData = getSensorData,

    move = move,
}

-- The module table acts as a proxy
__LIME_MODULE_TABLE__ = {}

setmetatable(__LIME_MODULE_TABLE__, {
    __index = moduleTable,
    __newindex = function (t, k, v)
        error("attempt to modify a read-only table")
    end
})