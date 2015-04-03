
--[[

This module lists constants used throughout the Deathmatch gamemode.
It is encouraged to create local copies of these constants for faster access.

TODO: work on making this file part of the common module group.

]]

local constants = {
    -- Attribute names

    attribEntityPosX = "EntityPosX", -- general attribute for retrieving position X
    attribEntityPosY = "EntityPosY", -- general attribute for retrieving position Y
    attribEntityAngle = "EntityAngle", -- general attribute for retrieving angle
    attribEntityVelX = "EntityVelX", -- general attribute for retrieving velocity X
    attribEntityVelY = "EntityVelY", -- general attribute for retrieving velocity Y
    attribEntityParent = "EntityParent", -- general attribute for retrieving the parent ID

    attribEntityCollector = "EntityCollector", -- if the value evaluates to true, an entity can pick up pickups
    attribEntityDamageable = "EntityDamageable", -- if the value evaluates to true, an entity is considered damageable
    attribEntityHealth = "EntityHealth", -- damageable entity's health

    attribLykkeFocusX = "LykkeFocusX",
    attribLykkeFocusY = "LykkeFocusY",
    attribLykkeAbilityWallSlide = "LykkeAbilityWallSlide", -- if the value evaluates to true, Lykke can wall slide
    attribLykkeAbilityWallJump = "LykkeAbilityWallJump", -- if the value evaluates to true, Lykke can wall jump

    attribBulletSpeed = "BulletSpeed", -- bullet's speed
    attribBulletTimeout = "BulletTimeout", -- bullet's timeout
    attribBulletRadius = "BulletRadius", -- bullet's radius
    attribBulletOnHit = "BulletOnHit", -- a callback for a bullet hitting a damageable target
    attribBulletOnWaste = "BulletOnWaste", -- a callback for a bullet hitting a non-damageable target

    -- Constants

    cWorldGravityX = 0.0,
    cWorldGravityY = -18.0,
    cWorldAmbientLightR = 0.01,
    cWorldAmbientLightG = 0.0,
    cWorldAmbientLightB = 0.1,

    cCameraPadding = 4,
    cCameraScaleX = 32,
    cCameraScaleY = 18,

    cLykkeWidth = 0.25,
    cLykkeHeight = 0.5,
    cLykkeMaxVelocityX = 12,
    cLykkeGroundAcceleration = 10,
    cLykkeAirAcceleration = 1.5,
    cLykkeMaxVelocityX = 12,
    cLykkeJumpImpulseY = 17,
    cLykkeWallJumpImpulseX = 8,
    cLykkeWallJumpImpulseYM = 0.7,
    cLykkeWallSlideVelYM = 8.7,

    cPickupWidth = 0.7,
}

-- The module table acts as a proxy
__LIME_MODULE_TABLE__ = {}

setmetatable(__LIME_MODULE_TABLE__, {
    __index = constants,
    __newindex = function (t, k, v)
        error("attempt to modify a read-only table")
    end
})
