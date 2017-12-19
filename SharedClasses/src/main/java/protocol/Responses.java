/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Алескандр
 * Ответы сервера на запросы
 * пока только OK и ERROR
 * мб расширю разными типами ошибок. можно еще пересылать исключения.
 */
@XmlRootElement
@XmlEnum(String.class)
public enum Responses {
    OK,ERROR
}
