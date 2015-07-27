events
    doorClosed D1CL
    drawerOpened D2OP
    lightOn L1ON
    doorOpened D1OP
    panel1Closed PNCL
end

resetEvents
    doorOpened
end

commands
    unlockPanel PNUL
    lockPanel PLLK
    lockDoor D1LK
    unlockDoor D1UL
end

state idle
    actions unlockDoor lockPanel
    doorClosed => active
end

state active
    drawerOpened => watingForLight
    lightOn => watingForDrawer
end

state watingForLight
    ligntOn => unlockedPanel
end

state watingForDrawer
    drawerOpened => unlockedPanel
end

state unlockedPanel
    actions unlockPanel lockDoor
    panelClosed => idle
end
