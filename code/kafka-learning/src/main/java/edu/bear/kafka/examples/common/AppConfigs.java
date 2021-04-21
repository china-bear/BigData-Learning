/*
 * Copyright (c) 2019. Prashant Kumar Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package edu.bear.kafka.examples.common;

/**
 * Application Configs
 *
 * @author prashant
 * @author www.learningjournal.guru
 */
public class AppConfigs {

    //public final static String applicationID = "HelloProducer";
    public final static String bootstrapServers = "10.146.237.9:9092";
    public final static String topicName = "hello-topic";
    public final static String groupName = "hello-group";
    public final static String autoCommitInterval = "1000";
    public final static int numEvents = 10;
}