/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Алескандр
 * Форматы команд(если чет не нравится - пишите, обсудим)
 * всякая команда начинается с послания ее кодового слова в xml формате
 * ответов от сервера может быть 2
 * OK - все хорошо, можно продолжать
 * ERROR - чет пошло не так. далее последует строка в xml содержащая инфу об ошибке. 
 * мб организую для них отдельный класс/перечисление
 * команды
 * ADD_AUTHOR - после команды передавате автора в XML. индекс будет установлен по прибытию.
 * ADD_BOOK - аналогично. только надо еще перед книгой id ее автора кинуть, к кому добавляется.для этого сделан класс Index. 
 * SET_BOOK - после команды посылайте индекс. после индекса, если книга не редактируется кем-то еще,
 * она будет заблокировна для остальных и получите OK. иначе получите ошибку.после получения блокировки, передавайте изменненую книгу.
 * SET_AUTHOR - аналогично. пока лучше не придумал.пока ни тот не другой set не работают.еще не сделал 
 * REMOVE_BOOK - Аналогично SET, разумеется без передачи новой книги в конце. пришлет ок если все удалилось как надо
 * REMOVE_AUTHOR - Аналогично 
 * VIEW_BOOKS - после OK последует сериализованный AuthorContainer. пока так.
 * VIEW_AUTHORS - аналогично.
 * BYE - посылайте как сигнал окончания сессии. ответит ок и разорвет сессию.
 */

@XmlRootElement
@XmlEnum(String.class)
public enum Commands {
    ADD_AUTHOR, ADD_BOOK, SET_AUTHOR, SET_BOOK, REMOVE_BOOK, REMOVE_AUTHOR, VIEW_BOOKS, VIEW_AUTHORS, BYE
}
