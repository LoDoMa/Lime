
-- Module input
local padding = 0
local widthMin = 1
local heightMin = 1
local focusPointsX = {}
local focusPointsY = {}

-- Module output
local translationX = 0
local translationY = 0
local scaleX = 0
local scaleY = 0

-- Module stuff
local targetTranslationX
local targetTranslationY
local targetScaleX
local targetScaleY

local function addFocusPoint(focusX, focusY)
    focusPointsX[#focusPointsX + 1] = focusX
    focusPointsY[#focusPointsY + 1] = focusY
end

local function updateCamera(timeDelta)
    local minX = nil
    local maxX = nil
    local minY = nil
    local maxY = nil

    for i = 1, #focusPointsX do
        local focusX = focusPointsX[i]
        local focusY = focusPointsY[i]

        if minX then
            if focusX < minX then minX = focusX end
            if focusX > maxX then maxX = focusX end
            if focusY < minY then minY = focusY end
            if focusY > maxY then maxY = focusY end
        else
            minX = focusX
            maxX = focusX
            minY = focusY
            maxY = focusY
        end

        focusPointsX[i] = nil
        focusPointsY[i] = nil
    end

    if not minX then
        translationX = 0
        translationY = 0
        scaleX = 0
        scaleY = 0
        -- no focus points, no update
        return
    end

    minX = minX - padding
    maxX = maxX + padding
    minY = minY - padding
    maxY = maxY + padding

    local width = maxX - minX
    local height = maxY - minY

    local centerX = minX + width / 2.0
    local centerY = minY + height / 2.0

    if width < widthMin then width = widthMin end
    if height < heightMin then height = heightMin end

    local ratio = width / height
    local targetRatio = widthMin / heightMin

    if ratio > targetRatio then
        height = width / targetRatio
    elseif ratio < targetRatio then
        width = height * targetRatio
    end

    targetTranslationX = centerX - width / 2.0
    targetTranslationY = centerY - height / 2.0
    targetScaleX = width
    targetScaleY = height

    translationX = translationX + (targetTranslationX - translationX) * timeDelta * 5
    translationY = translationY + (targetTranslationY - translationY) * timeDelta * 5
    scaleX = scaleX + (targetScaleX - scaleX) * timeDelta * 5
    scaleY = scaleY + (targetScaleY - scaleY) * timeDelta * 5
end

__LIME_MODULE_TABLE__ = {
    addFocusPoint = addFocusPoint,
    update = updateCamera,

    getTranslation = function () return translationX, translationY end,
    getScale = function () return scaleX, scaleY end,

    setPadding = function (padding_) padding = padding_ end,
    setMinimumScale = function (minw, minh)
        widthMin = minw
        heightMin = minh
        if widthMin == 0 then error("Minimum camera scale X may not be 0") end
        if heightMin == 0 then error("Minimum camera scale Y may not be 0") end
    end,
}
