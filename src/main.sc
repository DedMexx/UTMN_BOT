require: slotfilling/slotFilling.sc
  module = sys.zb-common

theme: /

    state: Welcome
        q!: $regex</start>
        q!: *start
        q!: (Привет*/здравствуй*)
        random:
            a: Здравствуйте!
            a: Приветствую!
        a: Я могу вам купить билет на самолёт, хорошо?
        state: Accepted
            q: (хорош*/ок*/да/верн*/давай*) *
            a: Отлично
            
        state: Rejected
            q: * (нет/не/ноу/отказ*/отмена/не надо) *
            a: Боюсь, я пока не могу предложить вам что-то ещё...
    
    state: CatchAll
        event!: noMatch
        a: Извините, я вас не понял. Переформулируйте, пожалуйста.