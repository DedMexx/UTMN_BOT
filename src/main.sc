require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
require: localPatterns.sc

require: phoneNumber/phoneNumber.sc
    module = sys.zb-common

theme: /

    state: Welcome
        q!: $regex</start>
        q!: *start
        q!: $hi
        script:
            $response.replies = $response.replies || [];
            $response.replies.push({
                type: "image",
                imageUrl: "http://apikabu.ru/img_n/2012-10_1/1jy.jpg",
                text: "Кот Гитлера"
            });
        random:
            a: Здравствуйте!
            a: Приветствую!
        a: Меня зовут {{$injector.botName}}
        go!: /SuggestHelp

    
    state: CatchAll || noContext = true
        event!: noMatch
        a: Извините, я вас не понял. Переформулируйте, пожалуйста.
    
    state: SuggestHelp
        q: Отмена || fromState = /AskPhone, onlyThisState = true
        a: Я могу вам купить билет на самолёт, хорошо?
        buttons:
            "Да" -> /SuggestHelp/Accepted
            "Нет" -> /SuggestHelp/Rejected
            
        state: Accepted
            q: (хорош*/ок*/да/верн*/давай*/ага/угу) *
            a: Отлично
            if: $client.phone
                go!: /Confirm
            else:
                go!: /AskPhone
            
        state: Rejected
            q: * (нет/не/ноу/отказ*/отмена/не надо) *
            a: Боюсь, я пока не могу предложить вам что-то ещё...
    
    state: AskPhone || modal = true
        a: Для продолжения введите, пожалуйста, ваш номер телефона:
        buttons:
            "Отмена"
            
        state: GetPhone
            q: * $mobilePhoneNumber *
            script:
                $temp.phone = $parseTree._mobilePhoneNumber;
            go!: /Confirm
        
        state: LocalCatchAll
            event: noMatch
            a: Что-то это не похоже на номер телефона...
            go: ..
            
    state: Confirm
        script:
            $temp.phone = $temp.phone || $client.phone;
        a: Ваш номер - {{$temp.phone}}, верно?
        script:
            $session.probablyPhone = $temp.phone;
        buttons:
            "Да"
            "Нет"
        
        state: Agree
            q: (да/верно)
            script:
                $client.phone = $session.probablyPhone;
                delete $session.probablyPhone;
                delete $session.Phone;
        
        state: Disagree
            q: (нет/но/неверно/не/неа/не верно/не мой)
            go!: /AskPhone