/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.bear.flink.examples.java.relational.util;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the default data sets used for the Weblog Analysis example program. The default data
 * sets are used, if no parameters are given to the program.
 */
public class WebLogData {

    public static final Object[][] DOCUMENTS = {
        new Object[] {
            "url_0",
            "dolor ad amet enim laoreet nostrud veniam aliquip ex nonummy diam dolore tincidunt tation exerci exerci wisi dolor nostrud "
        },
        new Object[] {
            "url_1",
            "wisi minim adipiscing nibh adipiscing ut nibh Lorem Ut nonummy euismod nibh wisi sit consectetuer exerci sed aliquip aliquip dolore aliquam enim dolore veniam aliquam euismod suscipit ad adipiscing exerci aliquip consectetuer euismod aliquip ad exerci ex nibh ex erat exerci laoreet lobortis quis "
        },
        new Object[] {
            "url_2",
            "diam sed convection aliquip amet commodo nonummy sed sed commodo commodo diam commodo adipiscing ad exerci magna exerci tation quis lobortis "
        },
        new Object[] {
            "url_3",
            "exerci suscipit sed lobortis amet lobortis aliquip nibh nostrud ad convection commodo ad nibh sed minim amet ad ea ea "
        },
        new Object[] {
            "url_4", "sit enim dolor quis laoreet ullamcorper veniam adipiscing ex quis commodo "
        },
        new Object[] {
            "url_5",
            "elit aliquip ea nisl oscillations sit dolor ipsum tincidunt ullamcorper dolore enim adipiscing laoreet elit ea volutpat adipiscing ea nibh nostrud Ut aliquam veniam Lorem laoreet veniam aliquip "
        },
        new Object[] {
            "url_6",
            "consectetuer ad sed suscipit euismod aliquip quis ullamcorper oscillations tation consectetuer tation amet suscipit nibh enim nonummy veniam commodo commodo diam euismod dolor Ut aliquip diam ex ad nonummy ad tincidunt minim exerci consectetuer veniam convection aliquam ut ut Lorem euismod sed ipsum volutpat "
        },
        new Object[] {
            "url_7",
            "Ut volutpat veniam ut consectetuer diam ut aliquam dolor nostrud erat consectetuer adipiscing exerci consectetuer Ut ullamcorper suscipit aliquam sed dolor nisl "
        },
        new Object[] {
            "url_8",
            "suscipit amet wisi nisl veniam lobortis sit Lorem aliquam nostrud aliquam ipsum ut laoreet suscipit Lorem laoreet editors adipiscing ullamcorper veniam erat consectetuer ut lobortis dolore elit sed tincidunt ipsum tation ullamcorper nonummy adipiscing ex ad laoreet ipsum suscipit lobortis lobortis Ut nonummy adipiscing erat volutpat aliquam "
        },
        new Object[] {
            "url_9",
            "nonummy commodo tation editors ut quis sit quis lobortis ea dolore oscillations diam ad dolor lobortis nisl ad veniam ullamcorper quis magna volutpat sit ipsum consectetuer dolore exerci commodo magna erat enim ut suscipit "
        },
        new Object[] {
            "url_10",
            "amet erat magna consectetuer tation tation aliquip nibh aliquam sed adipiscing ut commodo ex erat tincidunt aliquam ipsum Ut Ut sit tincidunt adipiscing suscipit minim sed erat dolor consectetuer Lorem consectetuer Lorem amet nibh diam ea ex enim suscipit wisi dolor nonummy magna enim euismod ullamcorper ut suscipit adipiscing "
        },
        new Object[] {
            "url_11",
            "ex quis exerci tation diam elit nostrud nostrud ut ipsum elit amet diam laoreet amet consectetuer volutpat sed lobortis "
        },
        new Object[] {
            "url_12",
            "elit suscipit sit ullamcorper ut ad erat ut dolor nostrud quis nisl enim erat dolor convection ad minim ut veniam nostrud sed editors adipiscing volutpat Ut aliquip commodo sed euismod adipiscing erat adipiscing dolore nostrud minim sed lobortis ea diam "
        },
        new Object[] {
            "url_13",
            "enim ut quis commodo veniam minim erat lobortis ad diam ex dolor tincidunt exerci ut aliquip tincidunt minim ut magna sed enim wisi veniam oscillations Lorem consectetuer "
        },
        new Object[] {
            "url_14",
            "nibh ipsum ullamcorper volutpat ut wisi dolor quis amet euismod quis ipsum ipsum minim tation volutpat sit exerci volutpat amet nonummy euismod veniam consectetuer sit consectetuer tincidunt nibh aliquam lobortis tation veniam ut ullamcorper wisi magna Ut volutpat consectetuer erat quis dolore ea tation "
        },
        new Object[] {
            "url_15",
            "ad wisi sed enim aliquam oscillations nibh Lorem lobortis veniam nibh laoreet nonummy sed nibh Lorem adipiscing diam magna nostrud magna oscillations ut oscillations elit nostrud diam editors Lorem "
        },
        new Object[] {
            "url_16",
            "nostrud volutpat veniam exerci tincidunt nostrud quis elit ipsum ea nonummy volutpat dolor elit lobortis magna nisl ut ullamcorper magna Lorem exerci nibh nisl magna editors erat aliquam aliquam ullamcorper sit aliquam sit nostrud oscillations consectetuer adipiscing suscipit convection exerci ea ullamcorper ex nisl "
        },
        new Object[] {
            "url_17",
            "ad ex aliquam erat aliquam elit veniam laoreet ut amet amet nostrud ut adipiscing Ut Lorem suscipit ex magna ullamcorper aliquam ullamcorper ullamcorper amet amet commodo aliquam volutpat nonummy nonummy tincidunt amet tation tincidunt volutpat ut veniam nisl erat dolor enim nonummy nostrud adipiscing laoreet adipiscing "
        },
        new Object[] {
            "url_18",
            "lobortis ipsum ex tincidunt tincidunt editors euismod consectetuer ipsum adipiscing lobortis exerci adipiscing nonummy nisl dolore nonummy erat exerci nisl ut dolore wisi volutpat lobortis magna "
        },
        new Object[] {
            "url_19", "ipsum tation laoreet tation adipiscing wisi nibh diam Ut suscipit ad wisi "
        },
        new Object[] {
            "url_20",
            "diam Lorem enim wisi ad lobortis dolor Ut ipsum amet dolore consectetuer nisl exerci nisl nonummy minim Ut erat oscillations ut Lorem nostrud dolore Ut dolore exerci ad ipsum dolore ex dolore aliquip sed aliquam ex aliquip magna amet ex dolore oscillations aliquip tation magna Ut "
        },
        new Object[] {
            "url_21",
            "lobortis ut amet ex nisl ullamcorper tincidunt ut elit diam quis suscipit ad amet ipsum magna Ut ex tincidunt "
        },
        new Object[] {
            "url_22",
            "amet commodo nisl ad quis lobortis ut commodo sit ut erat exerci lobortis suscipit nibh ut nostrud ut adipiscing commodo commodo quis quis nostrud nisl ipsum nostrud laoreet Lorem nostrud erat nostrud amet consectetuer laoreet oscillations wisi sit magna nibh amet "
        },
        new Object[] {
            "url_23",
            "adipiscing suscipit suscipit aliquip suscipit consectetuer minim magna ea erat nibh sit suscipit sed dolor oscillations nonummy volutpat ut tincidunt "
        },
        new Object[] {
            "url_24",
            "commodo sed tincidunt aliquip aliquip dolore commodo nonummy sed erat ut ex exerci dolore adipiscing tincidunt ex diam amet aliquam "
        },
        new Object[] {
            "url_25", "consectetuer consectetuer exerci quis ea veniam aliquam laoreet minim ex "
        },
        new Object[] {
            "url_26",
            "dolor exerci euismod minim magna quis erat consectetuer sed ex erat dolore quis ut oscillations ullamcorper Lorem exerci ex nibh ut exerci ullamcorper veniam nibh ut commodo ut Ut nostrud tincidunt tincidunt ad dolore Lorem ea tation enim erat nibh ut ea nonummy sed sed wisi nisl dolore "
        },
        new Object[] {
            "url_27",
            "amet elit ea ea nostrud editors Ut nostrud amet laoreet adipiscing ut nisl nonummy tincidunt ea ipsum ex dolore dolore oscillations sit minim Ut wisi ut laoreet minim elit "
        },
        new Object[] {
            "url_28",
            "wisi exerci volutpat Ut nostrud euismod minim Ut sit euismod ut ea magna consectetuer nisl ad minim tation nisl adipiscing Lorem aliquam quis exerci erat minim aliquip sit Lorem wisi wisi ut "
        },
        new Object[] {
            "url_29",
            "amet sed laoreet amet aliquam minim enim tincidunt Lorem sit aliquip amet suscipit ut laoreet elit suscipit erat ut tincidunt suscipit ipsum sed euismod elit dolore euismod dolore ut dolor nostrud ipsum tincidunt commodo adipiscing aliquam ut wisi dolor dolor suscipit "
        },
        new Object[] {
            "url_30",
            "euismod Lorem ex tincidunt amet enim minim suscipit exerci diam veniam amet nostrud ea ea "
        },
        new Object[] {
            "url_31",
            "ex ipsum sit euismod euismod ullamcorper tincidunt ut wisi ea adipiscing sed diam tation ipsum dolor aliquam veniam nonummy aliquip aliquip Lorem ut minim nisl tation sit exerci ullamcorper Ut dolor euismod aliquam consectetuer ad nonummy commodo exerci "
        },
        new Object[] {
            "url_32",
            "volutpat ipsum lobortis nisl veniam minim adipiscing dolor editors quis nostrud amet nostrud "
        },
        new Object[] {
            "url_33",
            "commodo wisi aliquip ut aliquam sed nostrud ex diam ad nostrud enim ut amet enim ea ad sed tation nostrud suscipit ea magna magna Lorem amet lobortis ut quis nibh aliquam aliquam exerci aliquip lobortis consectetuer enim wisi ea nisl laoreet erat dolore "
        },
        new Object[] {
            "url_34",
            "tincidunt adipiscing enim tation nibh Ut dolore tincidunt tation laoreet suscipit minim aliquam volutpat laoreet suscipit tincidunt nibh ut ut sit nostrud nonummy tincidunt exerci sit ad sed consectetuer minim dolor dolore laoreet nostrud nibh laoreet ea adipiscing exerci dolore ipsum "
        },
        new Object[] {
            "url_35",
            "tation ut erat ut tation dolor Lorem laoreet Lorem elit adipiscing wisi aliquip nostrud elit Ut volutpat ea aliquam aliquip "
        },
        new Object[] {
            "url_36",
            "lobortis enim ullamcorper adipiscing consectetuer aliquip wisi enim minim Ut minim elit elit aliquam exerci ullamcorper amet lobortis adipiscing diam laoreet consectetuer nostrud diam diam amet ut enim ullamcorper aliquip diam ut nostrud diam magna amet nonummy commodo wisi enim ullamcorper suscipit euismod dolore tincidunt magna suscipit elit "
        },
        new Object[] {
            "url_37",
            "elit adipiscing nisl nisl ex aliquip nibh sed ut ad Lorem elit consectetuer ad volutpat lobortis amet veniam ipsum nibh ut consectetuer editors ad aliquam "
        },
        new Object[] {
            "url_38",
            "elit quis nibh adipiscing sit consectetuer ut euismod quis tincidunt quis nisl consectetuer dolor diam suscipit quis dolore Lorem suscipit nonummy sed ex "
        },
        new Object[] {
            "url_39",
            "nisl sit consectetuer elit oscillations enim ipsum enim nostrud adipiscing nostrud editors aliquam "
        },
        new Object[] {
            "url_40",
            "sed wisi dolor diam commodo ullamcorper commodo nostrud ullamcorper laoreet minim dolore suscipit laoreet tation aliquip "
        },
        new Object[] {
            "url_41",
            "ad consectetuer exerci nisl exerci amet enim diam lobortis Lorem ex volutpat volutpat nibh aliquam ut ullamcorper volutpat nostrud ut adipiscing ullamcorper "
        },
        new Object[] {
            "url_42",
            "minim laoreet tation magna veniam ut ea sit ipsum tincidunt Ut amet ex aliquip ex euismod exerci wisi elit editors ad amet veniam ad editors "
        },
        new Object[] {
            "url_43",
            "ut nisl ad ullamcorper nibh Ut editors exerci enim exerci ea laoreet veniam ea amet exerci volutpat amet ad "
        },
        new Object[] {
            "url_44",
            "volutpat tincidunt enim amet sed tincidunt consectetuer ullamcorper nisl Ut adipiscing tation ad ad amet nonummy elit erat nibh Lorem erat elit laoreet consectetuer sed aliquip nostrud "
        },
        new Object[] {
            "url_45",
            "sed aliquam ut ut consectetuer wisi euismod enim erat euismod quis exerci amet tation sit "
        },
        new Object[] {
            "url_46",
            "lobortis oscillations tation aliquam dolore Lorem aliquip tation exerci ullamcorper aliquam aliquip lobortis ex tation dolor ut ut sed suscipit nisl ullamcorper sed editors laoreet aliquip enim dolor veniam tincidunt sed euismod tation "
        },
        new Object[] {
            "url_47",
            "Lorem Lorem ut wisi ad ut tation consectetuer exerci convection tation ullamcorper sed dolore quis aliquam ipsum lobortis commodo nonummy "
        },
        new Object[] {
            "url_48",
            "laoreet minim veniam nisl elit sit amet commodo ex ullamcorper suscipit aliquip laoreet convection Ut ex minim aliquam "
        },
        new Object[] {
            "url_49",
            "lobortis nonummy minim amet sit veniam quis consectetuer tincidunt laoreet quis "
        },
        new Object[] {
            "url_50",
            "lobortis nisl commodo dolor amet nibh editors enim magna minim elit euismod diam laoreet laoreet ad minim sed ut Ut lobortis adipiscing quis sed ut aliquam oscillations exerci tation consectetuer lobortis elit tincidunt consectetuer minim amet dolore quis aliquam Ut exerci sed aliquam quis quis ullamcorper Ut ex tincidunt "
        },
        new Object[] {
            "url_51",
            "nostrud nisl ea erat ut suscipit Ut sit oscillations ullamcorper nonummy magna lobortis dolore editors tincidunt nostrud suscipit ex quis tation ut sit amet nostrud laoreet ex tincidunt "
        },
        new Object[] {
            "url_52",
            "ea tation commodo elit sed ex sed quis enim nisl magna laoreet adipiscing amet sit nostrud consectetuer nibh tincidunt veniam ex veniam euismod exerci sed dolore suscipit nisl tincidunt euismod quis Ut enim euismod dolor diam exerci magna exerci ut exerci nisl "
        },
        new Object[] {
            "url_53",
            "volutpat amet Ut lobortis dolor tation minim nonummy lobortis convection nostrud "
        },
        new Object[] {
            "url_54",
            "ullamcorper commodo Ut amet sit nostrud aliquam ad amet wisi enim nostrud ipsum nisl veniam erat aliquam ex aliquam dolor dolor ut consectetuer euismod exerci elit exerci Ut ea minim enim consectetuer ad consectetuer nonummy convection adipiscing ad ullamcorper lobortis nonummy laoreet nonummy aliquam ullamcorper ad nostrud amet "
        },
        new Object[] {
            "url_55",
            "wisi magna editors amet aliquam diam amet aliquip nisl consectetuer laoreet nonummy suscipit euismod diam enim tation elit ut lobortis quis euismod suscipit nostrud ea ea commodo lobortis dolore Ut nisl nostrud dolor laoreet euismod ea dolore aliquam ut Lorem exerci ex sit "
        },
        new Object[] {
            "url_56",
            "ex dolor veniam wisi laoreet ut exerci diam ad ex ut ut laoreet ut nisl ullamcorper nisl "
        },
        new Object[] {
            "url_57",
            "diam adipiscing Ut ut Lorem amet erat elit erat magna adipiscing euismod elit ullamcorper nostrud aliquam dolor ullamcorper sit tation tation "
        },
        new Object[] {
            "url_58",
            "laoreet convection veniam lobortis dolore ut nonummy commodo erat lobortis veniam nostrud dolore minim commodo ut consectetuer magna erat ea dolore Lorem suscipit ex ipsum exerci sed enim ea tation suscipit enim adipiscing "
        },
        new Object[] {
            "url_59",
            "amet ut ut Ut ad dolor quis ad magna exerci suscipit magna nibh commodo euismod amet euismod wisi diam suscipit dolore Lorem dolor ex amet exerci aliquip ut ut lobortis quis elit minim sed Lorem "
        },
        new Object[] {
            "url_60",
            "ut ut amet ullamcorper amet euismod dolor amet elit exerci adipiscing sed suscipit sed exerci wisi diam veniam wisi suscipit ut quis nibh ullamcorper ex quis magna dolore volutpat editors minim ut sit aliquip oscillations nisl ipsum "
        },
        new Object[] {
            "url_61",
            "nibh nostrud tincidunt lobortis adipiscing adipiscing ullamcorper ullamcorper ipsum nisl ullamcorper aliquip laoreet commodo ut tation wisi diam commodo aliquip commodo suscipit tincidunt volutpat elit enim laoreet ut nostrud ad nonummy ipsum "
        },
        new Object[] {
            "url_62",
            "Ut ut minim enim amet euismod erat elit commodo consectetuer Ut quis dolor ex diam quis wisi tation tincidunt laoreet volutpat "
        },
        new Object[] {
            "url_63",
            "ut erat volutpat euismod amet ea nonummy lobortis ut Ut ea veniam sed veniam nostrud "
        },
        new Object[] {
            "url_64",
            "tation dolor suscipit minim nisl wisi consectetuer aliquip tation Ut commodo ut dolore consectetuer elit wisi nisl ipsum "
        },
        new Object[] {
            "url_65", "ullamcorper nisl Lorem magna tation veniam aliquam diam amet euismod "
        },
        new Object[] {
            "url_66",
            "euismod aliquam tincidunt Ut volutpat ea lobortis sit ut volutpat ut lobortis ut lobortis ut nisl amet dolor sed ipsum enim ullamcorper diam euismod nostrud wisi erat quis diam nibh Ut dolore sed amet tation enim diam "
        },
        new Object[] {
            "url_67",
            "amet minim minim amet laoreet Lorem aliquam veniam elit volutpat magna adipiscing enim enim euismod laoreet sed ex sed aliquam ad ea ut adipiscing suscipit ex minim dolore minim ea laoreet nisl "
        },
        new Object[] {
            "url_68",
            "aliquam ea volutpat ut wisi tation tation nibh nisl erat laoreet ea volutpat dolor dolor aliquam exerci quis ullamcorper aliquam ut quis suscipit "
        },
        new Object[] {
            "url_69",
            "quis exerci ut aliquip wisi dolore magna nibh consectetuer magna tation ullamcorper lobortis sed amet adipiscing minim suscipit nibh nibh nostrud euismod enim "
        },
        new Object[] {
            "url_70",
            "tation enim consectetuer adipiscing wisi laoreet diam aliquip nostrud elit nostrud aliquip ea minim amet diam dolore "
        },
        new Object[] {
            "url_71",
            "consectetuer tincidunt nibh amet tation nonummy sit tation diam sed diam tation "
        },
        new Object[] {
            "url_72",
            "Lorem ut nostrud nonummy minim quis euismod lobortis nostrud nonummy adipiscing tincidunt consectetuer ut nibh ad suscipit dolor ut elit dolore amet ut quis tation ullamcorper nonummy laoreet ullamcorper aliquam dolore convection dolor tincidunt ut ullamcorper ex dolor suscipit erat oscillations ad "
        },
        new Object[] {
            "url_73",
            "elit Ut commodo ut ullamcorper ullamcorper ut euismod commodo diam aliquip suscipit consectetuer exerci tation nostrud ut wisi exerci sed ut elit sed volutpat Lorem nibh laoreet consectetuer ex Lorem elit aliquam commodo lobortis ad "
        },
        new Object[] {
            "url_74",
            "quis magna laoreet commodo aliquam nisl ullamcorper veniam tation wisi consectetuer commodo consectetuer ad dolore aliquam dolor elit amet sit amet nibh commodo erat veniam aliquip dolore ad magna ad ipsum Ut exerci ea volutpat nisl amet nostrud sit "
        },
        new Object[] {
            "url_75",
            "tincidunt suscipit sit aliquip aliquam adipiscing dolore exerci Ut suscipit ut sit laoreet suscipit wisi sit enim nonummy consectetuer dolore editors "
        },
        new Object[] {
            "url_76",
            "veniam ullamcorper tation sit suscipit dolor suscipit veniam sit Lorem quis sed nostrud ad tincidunt elit adipiscing "
        },
        new Object[] {
            "url_77",
            "volutpat sit amet veniam quis ipsum nibh elit enim commodo magna veniam magna convection "
        },
        new Object[] {
            "url_78",
            "tation dolore minim elit nisl volutpat tation laoreet enim nostrud exerci dolore tincidunt aliquip Lorem ipsum nostrud quis adipiscing ullamcorper erat lobortis tation commodo Ut ipsum commodo magna ad ipsum ut enim "
        },
        new Object[] {
            "url_79",
            "lobortis amet elit Lorem amet nonummy commodo tation ex ea amet Lorem ea nonummy commodo veniam volutpat nibh wisi ad ipsum euismod ea convection nostrud nisl erat veniam Ut aliquip ad aliquip editors wisi magna tation nostrud nonummy adipiscing ullamcorper aliquip "
        },
        new Object[] {
            "url_80",
            "tincidunt nostrud nostrud magna ea euismod ea consectetuer nisl exerci ea dolor nisl commodo ex erat ipsum exerci suscipit ad nisl ea nonummy suscipit adipiscing laoreet sit euismod nibh adipiscing sed minim commodo amet "
        },
        new Object[] {
            "url_81",
            "nostrud erat ut sed editors erat amet magna lobortis diam laoreet dolor amet nibh ut ipsum ipsum amet ut sed ut exerci elit suscipit wisi magna ut veniam nisl commodo enim adipiscing laoreet ad Lorem oscillations "
        },
        new Object[] {
            "url_82",
            "quis commodo nibh nibh volutpat suscipit dolore magna tincidunt nibh ut ad ullamcorper ullamcorper quis enim ad ut tation minim laoreet veniam dolor sed tincidunt exerci exerci nostrud ullamcorper amet ut ut ullamcorper "
        },
        new Object[] {
            "url_83",
            "sit suscipit volutpat elit tation elit sed sed dolor ex ex ipsum euismod laoreet magna lobortis ad "
        },
        new Object[] {
            "url_84",
            "lobortis ipsum euismod enim ea tation veniam tation oscillations aliquip consectetuer euismod ut sed lobortis tation oscillations commodo euismod laoreet suscipit amet elit ullamcorper volutpat aliquam ea enim ullamcorper consectetuer laoreet tation quis ut commodo erat euismod dolor laoreet ullamcorper laoreet "
        },
        new Object[] {
            "url_85",
            "adipiscing sit quis commodo consectetuer quis enim euismod exerci nonummy ea nostrud Ut veniam sit aliquip nisl enim "
        },
        new Object[] {
            "url_86",
            "nostrud dolore veniam veniam wisi aliquip adipiscing diam sed quis ullamcorper "
        },
        new Object[] {
            "url_87",
            "quis Lorem suscipit Ut nibh diam euismod consectetuer lobortis ipsum sed suscipit consectetuer euismod laoreet ut wisi nisl elit quis commodo adipiscing adipiscing suscipit aliquam nisl quis magna ipsum enim ad quis ea magna Lorem nibh ea "
        },
        new Object[] {
            "url_88",
            "euismod commodo sed tincidunt Ut veniam consectetuer quis erat ex ea erat laoreet commodo nibh minim "
        },
        new Object[] {
            "url_89",
            "tation diam editors Ut enim nibh Lorem volutpat quis diam suscipit exerci wisi ad "
        },
        new Object[] {
            "url_90",
            "volutpat editors ea nibh wisi ad amet volutpat nisl ullamcorper nibh volutpat minim ex ut sit veniam Lorem consectetuer quis ad sit suscipit volutpat wisi diam sed tincidunt ipsum minim convection ea diam oscillations quis lobortis "
        },
        new Object[] {
            "url_91",
            "enim minim nonummy ea minim euismod adipiscing editors volutpat magna sit magna ut ipsum ut "
        },
        new Object[] {
            "url_92",
            "nisl Ut commodo amet euismod lobortis ea ea wisi commodo Lorem sit ipsum volutpat nonummy exerci erat elit exerci magna ad erat enim laoreet quis nostrud wisi ut veniam amet ullamcorper lobortis ad suscipit volutpat veniam nostrud nibh quis ipsum dolore consectetuer veniam ipsum aliquip dolore sed laoreet ipsum "
        },
        new Object[] {
            "url_93", "nonummy aliquam ad lobortis Lorem erat ad tation Lorem exerci ex "
        },
        new Object[] {
            "url_94",
            "nonummy dolore commodo exerci ex quis ut suscipit elit laoreet sit tation magna veniam ea sit nonummy veniam Lorem quis nibh aliquip exerci amet ullamcorper adipiscing erat nisl editors diam commodo ad euismod adipiscing ea suscipit exerci aliquip volutpat tation enim volutpat sit "
        },
        new Object[] {
            "url_95",
            "sit suscipit oscillations ipsum nibh dolor ea dolore ea elit ipsum minim editors magna consectetuer ullamcorper commodo nonummy sit nostrud aliquip sit erat ullamcorper ullamcorper nibh veniam erat quis dolore nonummy "
        },
        new Object[] {
            "url_96",
            "nostrud quis ut volutpat magna ad quis adipiscing Lorem commodo exerci laoreet magna adipiscing erat quis wisi ea ea laoreet enim convection ad dolor nisl amet nibh aliquam adipiscing tincidunt minim diam Lorem commodo adipiscing volutpat "
        },
        new Object[] {
            "url_97",
            "laoreet laoreet suscipit nostrud dolore adipiscing volutpat Ut sed nisl diam ullamcorper ex ut ut dolor amet nostrud euismod dolore veniam veniam enim tation veniam ea minim minim volutpat tincidunt "
        },
        new Object[] {
            "url_98",
            "quis lobortis amet wisi nostrud ipsum aliquam convection tincidunt dolore ullamcorper nibh lobortis volutpat ea nostrud oscillations minim nonummy enim ad lobortis exerci ipsum ullamcorper nibh nonummy diam amet enim veniam ut nostrud "
        },
        new Object[] {
            "url_99",
            "aliquam wisi suscipit commodo diam amet amet magna nisl enim nostrud tation nisl nostrud nibh ut "
        }
    };

    public static final Object[][] RANKS = {
        new Object[] {30, "url_0", 43},
        new Object[] {82, "url_1", 39},
        new Object[] {56, "url_2", 31},
        new Object[] {96, "url_3", 36},
        new Object[] {31, "url_4", 36},
        new Object[] {29, "url_5", 6},
        new Object[] {33, "url_6", 48},
        new Object[] {66, "url_7", 40},
        new Object[] {28, "url_8", 51},
        new Object[] {9, "url_9", 4},
        new Object[] {49, "url_10", 24},
        new Object[] {26, "url_11", 12},
        new Object[] {39, "url_12", 46},
        new Object[] {84, "url_13", 53},
        new Object[] {29, "url_14", 50},
        new Object[] {21, "url_15", 12},
        new Object[] {69, "url_16", 34},
        new Object[] {11, "url_17", 38},
        new Object[] {96, "url_18", 13},
        new Object[] {56, "url_19", 48},
        new Object[] {18, "url_20", 36},
        new Object[] {31, "url_21", 21},
        new Object[] {29, "url_22", 11},
        new Object[] {71, "url_23", 30},
        new Object[] {85, "url_24", 48},
        new Object[] {19, "url_25", 45},
        new Object[] {69, "url_26", 9},
        new Object[] {20, "url_27", 51},
        new Object[] {33, "url_28", 46},
        new Object[] {75, "url_29", 38},
        new Object[] {96, "url_30", 51},
        new Object[] {73, "url_31", 40},
        new Object[] {67, "url_32", 16},
        new Object[] {24, "url_33", 24},
        new Object[] {27, "url_34", 35},
        new Object[] {33, "url_35", 35},
        new Object[] {7, "url_36", 22},
        new Object[] {83, "url_37", 41},
        new Object[] {23, "url_38", 49},
        new Object[] {41, "url_39", 33},
        new Object[] {66, "url_40", 38},
        new Object[] {4, "url_41", 52},
        new Object[] {34, "url_42", 4},
        new Object[] {28, "url_43", 12},
        new Object[] {14, "url_44", 14},
        new Object[] {41, "url_45", 11},
        new Object[] {48, "url_46", 37},
        new Object[] {75, "url_47", 41},
        new Object[] {78, "url_48", 3},
        new Object[] {63, "url_49", 28}
    };

    public static final Object[][] VISITS = {
        new Object[] {"url_2", "2003-12-17"},
        new Object[] {"url_9", "2008-11-11"},
        new Object[] {"url_14", "2003-11-5"},
        new Object[] {"url_46", "2009-2-16"},
        new Object[] {"url_14", "2004-11-9"},
        new Object[] {"url_36", "2001-3-9"},
        new Object[] {"url_35", "2006-8-13"},
        new Object[] {"url_22", "2008-1-18"},
        new Object[] {"url_36", "2002-3-9"},
        new Object[] {"url_13", "2007-7-17"},
        new Object[] {"url_23", "2009-6-16"},
        new Object[] {"url_16", "2000-7-15"},
        new Object[] {"url_41", "2002-5-10"},
        new Object[] {"url_6", "2004-11-9"},
        new Object[] {"url_5", "2003-6-7"},
        new Object[] {"url_22", "2002-11-5"},
        new Object[] {"url_11", "2007-7-21"},
        new Object[] {"url_38", "2009-12-2"},
        new Object[] {"url_6", "2004-11-2"},
        new Object[] {"url_46", "2000-6-4"},
        new Object[] {"url_34", "2003-9-2"},
        new Object[] {"url_31", "2008-2-24"},
        new Object[] {"url_0", "2003-2-2"},
        new Object[] {"url_47", "2003-7-8"},
        new Object[] {"url_49", "2009-9-13"},
        new Object[] {"url_11", "2003-4-2"},
        new Object[] {"url_20", "2000-6-18"},
        new Object[] {"url_38", "2000-2-22"},
        new Object[] {"url_44", "2009-2-17"},
        new Object[] {"url_26", "2000-6-21"},
        new Object[] {"url_13", "2000-11-25"},
        new Object[] {"url_47", "2005-4-19"},
        new Object[] {"url_46", "2008-1-7"},
        new Object[] {"url_33", "2004-12-24"},
        new Object[] {"url_32", "2009-2-8"},
        new Object[] {"url_26", "2000-9-21"},
        new Object[] {"url_9", "2002-8-18"},
        new Object[] {"url_38", "2002-11-27"},
        new Object[] {"url_37", "2008-2-26"},
        new Object[] {"url_1", "2007-3-22"},
        new Object[] {"url_37", "2002-3-20"},
        new Object[] {"url_27", "2008-11-12"},
        new Object[] {"url_30", "2000-12-16"},
        new Object[] {"url_48", "2000-12-17"},
        new Object[] {"url_46", "2008-4-16"},
        new Object[] {"url_29", "2006-3-9"},
        new Object[] {"url_0", "2007-7-26"},
        new Object[] {"url_46", "2009-12-15"},
        new Object[] {"url_34", "2002-2-13"},
        new Object[] {"url_24", "2009-3-1"},
        new Object[] {"url_43", "2007-11-4"},
        new Object[] {"url_3", "2004-2-16"},
        new Object[] {"url_26", "2000-10-26"},
        new Object[] {"url_42", "2004-7-14"},
        new Object[] {"url_13", "2004-9-10"},
        new Object[] {"url_21", "2000-2-21"},
        new Object[] {"url_9", "2006-6-5"},
        new Object[] {"url_46", "2001-12-17"},
        new Object[] {"url_24", "2006-12-8"},
        new Object[] {"url_25", "2006-9-2"},
        new Object[] {"url_37", "2002-6-26"},
        new Object[] {"url_18", "2006-6-2"},
        new Object[] {"url_46", "2003-5-24"},
        new Object[] {"url_32", "2000-10-17"},
        new Object[] {"url_45", "2002-1-12"},
        new Object[] {"url_12", "2005-12-13"},
        new Object[] {"url_49", "2009-3-9"},
        new Object[] {"url_31", "2001-9-19"},
        new Object[] {"url_22", "2002-7-9"},
        new Object[] {"url_27", "2005-2-3"},
        new Object[] {"url_43", "2008-7-15"},
        new Object[] {"url_20", "2000-3-23"},
        new Object[] {"url_25", "2002-5-8"},
        new Object[] {"url_41", "2004-4-27"},
        new Object[] {"url_17", "2008-7-17"},
        new Object[] {"url_26", "2009-12-16"},
        new Object[] {"url_34", "2006-2-10"},
        new Object[] {"url_8", "2009-4-14"},
        new Object[] {"url_16", "2000-2-24"},
        new Object[] {"url_2", "2009-2-10"},
        new Object[] {"url_35", "2003-2-24"},
        new Object[] {"url_34", "2008-3-16"},
        new Object[] {"url_27", "2005-1-5"},
        new Object[] {"url_8", "2008-12-10"},
        new Object[] {"url_38", "2009-2-11"},
        new Object[] {"url_38", "2006-11-3"},
        new Object[] {"url_47", "2003-2-13"},
        new Object[] {"url_8", "2008-11-17"},
        new Object[] {"url_26", "2009-5-11"},
        new Object[] {"url_12", "2007-11-26"},
        new Object[] {"url_10", "2003-1-13"},
        new Object[] {"url_8", "2005-9-23"},
        new Object[] {"url_42", "2001-4-5"},
        new Object[] {"url_30", "2009-12-10"},
        new Object[] {"url_2", "2003-1-3"},
        new Object[] {"url_2", "2009-2-19"},
        new Object[] {"url_7", "2000-6-25"},
        new Object[] {"url_15", "2004-9-26"},
        new Object[] {"url_25", "2009-10-5"},
        new Object[] {"url_23", "2009-8-9"},
        new Object[] {"url_27", "2004-4-3"},
        new Object[] {"url_37", "2008-6-9"},
        new Object[] {"url_9", "2002-5-25"},
        new Object[] {"url_43", "2009-5-18"},
        new Object[] {"url_21", "2008-4-19"},
        new Object[] {"url_12", "2001-12-25"},
        new Object[] {"url_16", "2006-9-25"},
        new Object[] {"url_27", "2002-1-2"},
        new Object[] {"url_2", "2009-1-21"},
        new Object[] {"url_31", "2009-3-20"},
        new Object[] {"url_42", "2002-3-1"},
        new Object[] {"url_31", "2001-11-26"},
        new Object[] {"url_20", "2003-5-15"},
        new Object[] {"url_32", "2004-1-22"},
        new Object[] {"url_28", "2008-9-16"},
        new Object[] {"url_27", "2006-7-3"},
        new Object[] {"url_11", "2008-12-26"},
        new Object[] {"url_15", "2004-8-16"},
        new Object[] {"url_34", "2002-10-5"},
        new Object[] {"url_44", "2000-2-15"},
        new Object[] {"url_9", "2000-10-23"},
        new Object[] {"url_45", "2005-4-24"},
        new Object[] {"url_0", "2006-8-7"},
        new Object[] {"url_48", "2003-8-7"},
        new Object[] {"url_8", "2007-12-13"},
        new Object[] {"url_42", "2003-8-2"},
        new Object[] {"url_25", "2008-3-5"},
        new Object[] {"url_3", "2007-3-9"},
        new Object[] {"url_49", "2003-10-7"},
        new Object[] {"url_18", "2007-12-6"},
        new Object[] {"url_3", "2006-7-5"},
        new Object[] {"url_27", "2000-9-14"},
        new Object[] {"url_42", "2002-10-20"},
        new Object[] {"url_44", "2007-1-13"},
        new Object[] {"url_6", "2003-1-21"},
        new Object[] {"url_40", "2009-10-20"},
        new Object[] {"url_28", "2009-6-17"},
        new Object[] {"url_22", "2000-2-17"},
        new Object[] {"url_3", "2005-1-15"},
        new Object[] {"url_9", "2008-12-9"},
        new Object[] {"url_9", "2005-2-19"},
        new Object[] {"url_28", "2000-4-22"},
        new Object[] {"url_44", "2001-9-9"},
        new Object[] {"url_43", "2008-6-21"},
        new Object[] {"url_39", "2008-5-9"},
        new Object[] {"url_15", "2006-9-15"},
        new Object[] {"url_23", "2001-12-18"},
        new Object[] {"url_14", "2002-5-23"},
        new Object[] {"url_11", "2007-7-11"},
        new Object[] {"url_34", "2000-12-8"},
        new Object[] {"url_47", "2005-7-3"},
        new Object[] {"url_38", "2004-3-26"},
        new Object[] {"url_19", "2003-9-14"},
        new Object[] {"url_24", "2007-7-16"},
        new Object[] {"url_40", "2008-8-21"},
        new Object[] {"url_17", "2007-12-4"},
        new Object[] {"url_25", "2006-6-24"},
        new Object[] {"url_2", "2000-10-8"},
        new Object[] {"url_12", "2008-6-10"},
        new Object[] {"url_11", "2004-11-24"},
        new Object[] {"url_13", "2005-11-3"},
        new Object[] {"url_43", "2005-1-2"},
        new Object[] {"url_14", "2008-6-12"},
        new Object[] {"url_43", "2001-8-27"},
        new Object[] {"url_45", "2000-3-3"},
        new Object[] {"url_0", "2006-9-27"},
        new Object[] {"url_22", "2007-12-18"},
        new Object[] {"url_25", "2006-4-4"},
        new Object[] {"url_32", "2001-6-25"},
        new Object[] {"url_6", "2007-6-9"},
        new Object[] {"url_8", "2009-10-3"},
        new Object[] {"url_15", "2003-2-23"},
        new Object[] {"url_37", "2000-5-6"},
        new Object[] {"url_27", "2004-3-21"},
        new Object[] {"url_17", "2005-6-20"},
        new Object[] {"url_2", "2004-2-27"},
        new Object[] {"url_36", "2005-3-16"},
        new Object[] {"url_1", "2009-12-3"},
        new Object[] {"url_9", "2004-4-27"},
        new Object[] {"url_18", "2009-5-26"},
        new Object[] {"url_31", "2000-9-21"},
        new Object[] {"url_12", "2008-9-25"},
        new Object[] {"url_2", "2004-2-16"},
        new Object[] {"url_28", "2008-11-12"},
        new Object[] {"url_28", "2001-6-26"},
        new Object[] {"url_12", "2006-3-15"},
        new Object[] {"url_0", "2009-3-1"},
        new Object[] {"url_36", "2006-10-13"},
        new Object[] {"url_15", "2004-11-5"},
        new Object[] {"url_32", "2008-2-11"},
        new Object[] {"url_19", "2009-8-3"},
        new Object[] {"url_2", "2006-8-6"},
        new Object[] {"url_11", "2009-10-13"},
        new Object[] {"url_21", "2002-9-14"},
        new Object[] {"url_18", "2000-11-2"},
        new Object[] {"url_35", "2006-5-15"},
        new Object[] {"url_11", "2006-2-18"},
        new Object[] {"url_0", "2001-4-25"},
        new Object[] {"url_14", "2009-4-8"},
        new Object[] {"url_16", "2009-4-7"}
    };

    public static DataSet<Tuple2<String, String>> getDocumentDataSet(ExecutionEnvironment env) {

        List<Tuple2<String, String>> data = new ArrayList<Tuple2<String, String>>(100);
        for (Object[] document : DOCUMENTS) {
            data.add(new Tuple2<String, String>((String) document[0], (String) document[1]));
        }

        return env.fromCollection(data);
    }

    public static DataSet<Tuple3<Integer, String, Integer>> getRankDataSet(
            ExecutionEnvironment env) {

        List<Tuple3<Integer, String, Integer>> data =
                new ArrayList<Tuple3<Integer, String, Integer>>(100);
        for (Object[] rank : RANKS) {
            data.add(
                    new Tuple3<Integer, String, Integer>(
                            (Integer) rank[0], (String) rank[1], (Integer) rank[2]));
        }
        return env.fromCollection(data);
    }

    public static DataSet<Tuple2<String, String>> getVisitDataSet(ExecutionEnvironment env) {

        List<Tuple2<String, String>> data = new ArrayList<Tuple2<String, String>>(100);

        for (Object[] visit : VISITS) {
            data.add(new Tuple2<String, String>((String) visit[0], (String) visit[1]));
        }
        return env.fromCollection(data);
    }
}