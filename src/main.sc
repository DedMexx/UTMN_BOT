require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
require: localPatterns.sc

theme: /

    state: Welcome
        q!: $regex</start>
        q!: *start
        q!: $hi
        random:
            a: Здравствуйте!
            a: Приветствую!
        go!: /SuggestHelp

    
    state: CatchAll || noContext = true
        event!: noMatch
        a: Извините, я вас не понял. Переформулируйте, пожалуйста.
    
    state: SuggestHelp
        q: Отмена || fromState = /AskPhone
        a: Я могу вам купить билет на самолёт, хорошо?
        buttons:
            "Да" -> /SuggestHelp/Accepted
            "Нет" -> /SuggestHelp/Rejected
            
        state: Accepted
            q: (хорош*/ок*/да/верн*/давай*/ага/угу) *
            a: Отлично
            go!: /AskPhone
            
        state: Rejected
            q: * (нет/не/ноу/отказ*/отмена/не надо) *
            a: Боюсь, я пока не могу предложить вам что-то ещё...
    
    state: AskPhone || modal = true
        a: Для продолжения введите, пожалуйста, ваш номер телефона:
        buttons:
            "Отмена"