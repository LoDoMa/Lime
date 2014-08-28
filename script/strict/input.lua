
local strict = getStrict()
strictRequireJava("net.lodoma.lime.input.Input")

local Input = strict.java["net.lodoma.lime.input.Input"]

local function getMouseButton(button)
    return Input:getMouse(button)
end

local function getMouseButtonDown(button)
    return Input:getMouseDown(button)
end

local function getMouseButtonUp(button)
    return Input:getMouseUp(button)
end

local function getMousePosition()
    local javaPosition = Input:getMousePosition()
    return strict.vector.toLua(javaPosition)
end

local function getKeyboard(key)
    return Input:getKey(key)
end

local function getKeyboardDown(key)
    return Input:getKeyDown(key)
end

local function getKeyboardUp(key)
    return Input:getKeyUp(key)
end

local function getKeyboardRepeated(key)
    return Input:getKeyRepeated(key)
end

addToLime({
    input = {
        mouse = {
            button = {
                left   = 0,
                right  = 1,
                middle = 2,
            },
            get = getMouseButton,
            getDown = getMouseButtonDown,
            getUp = getMouseButtonUp,
            getPos = getMousePosition,
        },
        keyboard = {
            key = {
                none         = 0x00,
                escape       = 0x01,
                ["1"]        = 0x02,
                ["2"]        = 0x03,
                ["3"]        = 0x04,
                ["4"]        = 0x05,
                ["5"]        = 0x06,
                ["6"]        = 0x07,
                ["7"]        = 0x08,
                ["8"]        = 0x09,
                ["9"]        = 0x0A,
                ["0"]        = 0x0B,
                minus        = 0x0C,
                equals       = 0x0D,
                back         = 0x0E,
                tab          = 0x0F,
                q            = 0x10,
                w            = 0x11,
                e            = 0x12,
                r            = 0x13,
                t            = 0x14,
                y            = 0x15,
                u            = 0x16,
                i            = 0x17,
                o            = 0x18,
                p            = 0x19,
                lbracket     = 0x1A,
                rbracket     = 0x1B,
                ["return"]   = 0x1C,
                lcontrol     = 0x1D,
                a            = 0x1E,
                s            = 0x1F,
                d            = 0x20,
                f            = 0x21,
                g            = 0x22,
                h            = 0x23,
                j            = 0x24,
                k            = 0x25,
                l            = 0x26,
                semicolon    = 0x27,
                apostrophe   = 0x28,
                grave        = 0x29,
                lshift       = 0x2A,
                backslash    = 0x2B,
                z            = 0x2C,
                x            = 0x2D,
                c            = 0x2E,
                v            = 0x2F,
                b            = 0x30,
                n            = 0x31,
                m            = 0x32,
                comma        = 0x33,
                period       = 0x34,
                slash        = 0x35,
                rshift       = 0x36,
                multiply     = 0x37,
                lmenu        = 0x38,
                lalt         = 0x38, -- lmenu
                space        = 0x39,
                capital      = 0x3A,
                f1           = 0x3B,
                f2           = 0x3C,
                f3           = 0x3D,
                f4           = 0x3E,
                f5           = 0x3F,
                f6           = 0x40,
                f7           = 0x41,
                f8           = 0x42,
                f9           = 0x43,
                f10          = 0x44,
                numlock      = 0x45,
                scroll       = 0x46,
                numpad7      = 0x47,
                numpad8      = 0x48,
                numpad9      = 0x49,
                subtract     = 0x4A,
                numpad4      = 0x4B,
                numpad5      = 0x4C,
                numpad6      = 0x4D,
                add          = 0x4E,
                numpad1      = 0x4F,
                numpad2      = 0x50,
                numpad3      = 0x51,
                numpad0      = 0x52,
                decimal      = 0x53,
                f11          = 0x57,
                f12          = 0x58,
                f13          = 0x64,
                f14          = 0x65,
                f15          = 0x66,
                kana         = 0x70,
                convert      = 0x79,
                noconvert    = 0x7B,
                yen          = 0x7D,
                numpadequals = 0x8D,
                circumflex   = 0x90,
                at           = 0x91,
                colon        = 0x92,
                underline    = 0x93,
                kanji        = 0x94,
                stop         = 0x95,
                ax           = 0x96,
                unlabeled    = 0x97,
                numpadenter  = 0x9C,
                rcontrol     = 0x9D,
                numpadcomma  = 0xB3,
                divide       = 0xB5,
                sysrq        = 0xB7,
                rmenu        = 0xB8,
                ralt         = 0xB8, -- rmenu
                pause        = 0xC5,
                home         = 0xC7,
                up           = 0xC8,
                prior        = 0xC9,
                left         = 0xCB,
                right        = 0xCD,
                ["end"]      = 0xCF,
                down         = 0xD0,
                next         = 0xD1,
                insert       = 0xD2,
                delete       = 0xD3,
                lmeta        = 0xDB,
                lwin         = 0xDB, -- lmeta
                rmeta        = 0xDC,
                rwin         = 0xDC, -- rmeta
                apps         = 0xDD,
                power        = 0xDE,
                sleep        = 0xDF,
            },
            get = getKeyboard,
            getDown = getKeyboardDown,
            getUp = getKeyboardUp,
            getRepeated = getKeyboardRepeated,
        },
    },
})
