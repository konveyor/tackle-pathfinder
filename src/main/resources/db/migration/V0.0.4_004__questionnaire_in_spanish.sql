INSERT INTO translated_text (id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false,
       'Category_' || id || '_name', 'Detalles de la aplicación', 'ES'
FROM category
WHERE name = 'Application details' and deleted is not true;

INSERT INTO translated_text (id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false,
       'Category_' || id || '_name', 'Dependencias de la aplicación', 'ES'
FROM category
WHERE name = 'Application dependencies' and deleted is not true;

INSERT INTO translated_text (id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false,
       'Category_' || id || '_name', 'Arquitectura de la aplicación', 'ES'
FROM category
WHERE name = 'Application architecture' and deleted is not true;

INSERT INTO translated_text (id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false,
       'Category_' || id || '_name', 'Observabilidad de la aplicación', 'ES'
FROM category
WHERE name = 'Application observability' and deleted is not true;

INSERT INTO translated_text (id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false,
       'Category_' || id || '_name', 'Problemas transversales de la aplicación', 'ES'
FROM category
WHERE name = 'Application cross-cutting concerns' and deleted is not true;



INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_question', '¿El equipo de desarrollo de la aplicación entiende y desarrolla la aplicación activamente?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.question_text='Does the application development team understand and actively develop the application?' and
        c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_question', '¿Cuál es el soporte de la aplicación en producción?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.question_text='How is the application supported in production?' and
        c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_question', '¿Cuánto tiempo pasa desde que el código es consolidado hasta que la aplicación es desplegada en producción?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.question_text='How much time passes from when code is committed until the application is deployed to production?' and
        c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_question', '¿Con qué frecuencia se despliega la aplicación en producción?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.question_text='How often is the application deployed to production?' and
        c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_question', '¿Cuál es el tiempo medio de recuperación (MTTR) de una falla en un entorno de producción?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.question_text='What is the application''s mean time to recover (MTTR) from failure in a production environment?' and
       c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿La aplicación tiene requisitos legales y/o de licenciamiento?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='Does the application have legal and/or licensing requirements?' and
       c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Qué modelo describe mejor la arquitectura de la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='Which model best describes the application architecture?' and
       c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿La aplicación requiere de hardware específico?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='Does the application require specific hardware?' and
       c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Qué sistema operativo requiere la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='What operating system does the application require?' and
       c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿El vendedor provee soporte para un componente de terceros ejecutándose en un contenedor?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='Does the vendor provide support for a third-party component running in a container?' and
       c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', 'Dependencias entrantes', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='Incoming/northbound dependencies' and
       c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', 'Dependencias salientes', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='Outgoing/southbound dependencies' and
       c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Qué tan flexible es la aplicación? ¿Cómo se recupera de cortes y reinicios?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How resilient is the application? How well does it recover from outages and restarts?' and
       c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Cómo se comunica el mundo externo con la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How does the external world communicate with the application?' and
       c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Cómo gestiona la aplicación su estado interno?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How does the application manage its internal state?' and
       c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Cómo gestiona la aplicación el descubrimiento de servicios?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How does the application handle service discovery?' and
       c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Cómo se gestiona el ''clustering'' de la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How is the application clustering managed?' and
       c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Cómo usa la aplicación el logging y cómo se accede a esos logs?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How does the application use logging and how are the logs accessed?' and
       c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿La aplicación provee métricas?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='Does the application provide metrics?' and
       c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Cuán fácil es determinar la salud y disponibilidad de la aplicación para manejar tráfico?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How easy is it to determine the application''s health and readiness to handle traffic?' and
    c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_question', '¿Qué opción describe mejor las características de ejecución de la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.question_text='What best describes the application''s runtime characteristics?' and
       c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Cuánto tarda la aplicación en estar disponible para manejar tráfico?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How long does it take the application to be ready to handle traffic?' and
       c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Cómo se testea la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How is the application tested?' and
       c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Cómo se configura la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How is the application configured?' and
       c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Cómo se despliega la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How is the application deployed?' and
       c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Dónde se despliega la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='Where is the application deployed?' and
       c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Cuán maduro es el proceso de containerización, si existe?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How mature is the containerization process, if any?' and
       c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_question', '¿Cómo la aplicación adquiere las claves de seguridad o los certificados?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text='How does the application acquire security keys or certificates?' and
       c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_description', '¿Cuál es el nivel de conocimiento que el equipo de desarrollo posee sobre el desarrollo o uso de la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.description='How much knowledge does the team have about the application''s development or usage?' and
       c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', '¿El equipo tiene suficiente conocimiento para dar soporte a la aplicación en producción?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='Does the team have sufficient knowledge to support the application in production?' and
       c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', '¿Cuál es la latencia de desarrollo?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='What is the development latency?' and
       c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', 'Frecuencia de desarrollo', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='Deployment frequency' and
       c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', 'Tiempo promedio para que la aplicación se recupere de un fallo', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='Average time for the application to recover from failure' and
       c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', 'Requerimientos legales y de licenciamiento han de ser valorados para determinar su posible impacto (coste, reporte de fallos) en la plataforma de contenedores que hospeda la aplicación. Ejemplos de requerimientos legales: clusters aislados, certificaciones, cumplimiento con el ''Payment Card Industry Data Security Standard'' o el ''Health Insurance Portability and Accountability Act''. Ejemplos de requerimientos de licenciamiento: por servidor, por CPU.', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='Legal and licensing requirements must be assessed to determine their possible impact (cost, fault reporting) on the container platform hosting the application. Examples of legal requirements: isolated clusters, certifications, compliance with the Payment Card Industry Data Security Standard or the Health Insurance Portability and Accountability Act. Examples of licensing requirements: per server, per CPU.' and
       c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', 'Describa la arquitectura de la aplicación en términos simples.', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='Describe the application architecture in simple terms.' and
       c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', 'OpenShift Container Platform solo se ejecuta en x86, IBM Power, or IBM Z systems', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='OpenShift Container Platform runs only on x86, IBM Power, or IBM Z systems' and
       c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', 'Solo Linux y ciertas versiones de Microsoft Windows están soportadas en contenedores. Compruebe las últimas versiones y requerimientos.', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='Only Linux and certain Microsoft Windows versions are supported in containers. Check the latest versions and requirements.' and
       c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', '¿El vendedor soportará un componente si lo ejecuta en un contenedor?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='Will the vendor support a component if you run it in a container?' and
       c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', 'Sistemas o aplicaciones que llaman la aplicación', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='Systems or applications that call the application' and
       c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', 'Sistemas o aplicaciones a los cuales llama la aplicación', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='Systems or applications that the application calls' and
       c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', 'Si la aplicación o una de sus dependencias falla, ¿cómo se recupera la aplicación del fallo? ¿Se requiere intervención manual?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='If the application or one of its dependencies fails, how does the application recover from failure? Is manual intervention required?' and
       c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', '¿Qué protocolos usan los clientes externos para comunicarse con la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='What protocols do external clients use to communicate with the application?' and
       c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', '¿Si la aplicación debe gestionar o retener estado interno, cómo lo hace?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='If the application must manage or retain an internal state, how is this done?' and
       c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', '¿De qué forma la aplicación descubre servicios?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='How does the application discover services?' and
       c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', '¿La aplicación requiere clusters? Si es así, ¿cómo se gestiona el cluster?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='Does the application require clusters? If so, how is clustering managed?' and
       c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', '¿Cómo se accede a los logs de la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='How the application logs are accessed' and
       c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', '¿Las métricas de la aplicación están disponibles, si son  necesarias (ejemplo: OpenShift Container Platform recoge métricas de CPU y memory)?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='Are application metrics available, if necessary (example: OpenShift Container Platform collects CPU and memory metrics)?' and
       c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'Question_' || question.id || '_description', '¿Cómo se determina la salud de la aplicación (liveness) y la disponibilidad para manejar tráfico?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description='How do we determine an application''s health (liveness) and readiness to handle traffic?' and
    c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_description', '¿Cómo aparecería el perfil de una aplicación durante su ejecución (ejemplos: gráficas mostrando el uso de CPU y memoria, patrones de tráfico, latencia)? ¿Cuales son las implicaciones para una aplicación serverless?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.description='How would the profile of an application appear during runtime (examples: graphs showing CPU and memory usage, traffic patterns, latency)? What are the implications for a serverless application?' and
        c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_description', '¿Cuánto tarda la aplicación en arrancar?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.description='How long the application takes to boot' and
        c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_description', '¿Se verifica la aplicación? ¿Es fácil de verificar (ejemplo: verificación automatizada)? ¿Se verifica en producción?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.description='Is the application is tested? Is it easy to test (example: automated testing)? Is it tested in production?' and
        c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_description', '¿Cómo se configura la aplicación? ¿Es el método de configuración apropiado para un contenedor? Los servidores externos son dependencias de ejecución.', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.description='How is the application configured? Is the configuration method appropriate for a container? External servers are runtime dependencies.' and
        c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_description', '¿Cómo se despliega la aplicación? ¿Es el proceso de despliegue apropiado para una plataforma de contenedores?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.description='How the application is deployed and whether the deployment process is suitable for a container platform' and
        c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_description', '¿Dónde se ejecuta la aplicación?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.description='Where does the application run?' and
        c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_description', '¿Si el equipo ha usado contenedores en el pasado, cómo lo hizo?', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.description='If the team has used containers in the past, how was it done?' and
        c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'Question_' || question.id || '_description', '¿Cómo obtiene credenciales la aplicación, claves, o certificados? Sistemas externos son dependencias de ejecución.', 'ES'
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
        question.description='How does the application retrieve credentials, keys, or certificates? External systems are runtime dependencies.' and
        c.name = 'Application cross-cutting concerns';


INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_4_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'Does the application development team understand and actively develop the application?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_11_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'How is the application supported in production?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_18_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'How much time passes from when code is committed until the application is deployed to production?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_19_option', 'No registrado', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Not tracked'
  and question.question_text = 'How much time passes from when code is committed until the application is deployed to production?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_26_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'How often is the application deployed to production?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_27_option', 'No registrado', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Not tracked'
  and question.question_text = 'How often is the application deployed to production?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_34_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'What is the application''s mean time to recover (MTTR) from failure in a production environment?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_35_option', 'No registrado', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Not tracked'
       and question.question_text = 'What is the application''s mean time to recover (MTTR) from failure in a production environment?'
    and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_41_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'Does the application have legal and/or licensing requirements?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_47_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'Which model best describes the application architecture?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_52_option', 'Componentes independientemente desplegables', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Independently deployable components'
  and question.question_text = 'Which model best describes the application architecture?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_55_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'Does the application require specific hardware?'
  and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_61_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'What operating system does the application require?'
  and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_65_option', 'Microsoft Windows', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Microsoft Windows'
  and question.question_text = 'What operating system does the application require?'
  and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_68_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'Does the vendor provide support for a third-party component running in a container?'
  and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_76_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'Incoming/northbound dependencies'
  and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_80_option', 'Solo dependencias internas', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Internal dependencies only'
  and question.question_text = 'Incoming/northbound dependencies'
  and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_83_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'Outgoing/southbound dependencies'
  and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_91_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'How resilient is the application? How well does it recover from outages and restarts?'
  and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_6_option', 'Modo mantenimiento, sin conocimiento SME (Subject Matter Expert) o documentación adecuada disponible', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Maintenance mode, no SME knowledge or adequate documentation available'
  and question.question_text = 'Does the application development team understand and actively develop the application?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_98_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'How does the external world communicate with the application?'
  and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_105_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'How does the application manage its internal state?'
  and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_108_option', 'Estado mantenido en almacenaje no compartido y no efímero', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'State maintained in non-shared, non-ephemeral storage'
  and question.question_text = 'How does the application manage its internal state?'
  and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_112_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'How does the application handle service discovery?'
  and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_119_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'How is the application clustering managed?'
  and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_126_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'How does the application use logging and how are the logs accessed?'
  and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_134_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'Does the application provide metrics?'
  and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_141_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'How easy is it to determine the application''s health and readiness to handle traffic?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_142_option', 'Sin consulta de salud o disponibilidad', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'No health or readiness query functionality available'
       and question.question_text = 'How easy is it to determine the application''s health and readiness to handle traffic?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_144_option', 'La salud básica de la aplicación requiere scripting semi complejo', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Basic application health requires semi-complex scripting'
  and question.question_text = 'How easy is it to determine the application''s health and readiness to handle traffic?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_145_option', 'Endpoints independientes y dedicados sobre salud y disponibilidad', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Dedicated, independent liveness and readiness endpoints'
       and question.question_text = 'How easy is it to determine the application''s health and readiness to handle traffic?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_148_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Unknown'
  and question.question_text = 'What best describes the application''s runtime characteristics?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_155_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Unknown'
       and question.question_text = 'How long does it take the application to be ready to handle traffic?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_163_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Unknown'
       and question.question_text = 'How is the application tested?'
       and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_170_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Unknown'
       and question.question_text = 'How is the application configured?'
       and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_99_option', 'Protocolos no TCP/IP (ejemplos: serie, IPX, AppleTalk)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Non-TCP/IP protocols (examples: serial, IPX, AppleTalk)'
       and question.question_text = 'How does the external world communicate with the application?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_178_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Unknown'
       and question.question_text = 'How does the application acquire security keys or certificates?'
       and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_187_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Unknown'
       and question.question_text = 'How is the application deployed?'
       and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_190_option', 'Scripts de despliegue sencillos y automatizados', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Simple automated deployment scripts'
       and question.question_text = 'How is the application deployed?'
       and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_195_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Unknown'
       and question.question_text = 'Where is the application deployed?'
       and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_196_option', 'Servidor bare metal', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Bare metal server'
       and question.question_text = 'Where is the application deployed?'
       and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_203_option', 'Desconocido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Unknown'
       and question.question_text = 'How mature is the containerization process, if any?'
       and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_5_option', 'Poco conocimiento, sin desarrollo (ejemplo: aplicación de terceros o comercial de fábrica)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Little knowledge, no development (example: third-party or commercial off-the-shelf application)'
       and question.question_text = 'Does the application development team understand and actively develop the application?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_7_option', 'Modo mantenimiento, conocimiento SME (Subject Matter Expert) disponible', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Maintenance mode, SME knowledge is available'
       and question.question_text = 'Does the application development team understand and actively develop the application?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_8_option', 'Activamente desarrollada, conocimiento SME (Subject Matter Expert) disponible', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Actively developed, SME knowledge is available'
       and question.question_text = 'Does the application development team understand and actively develop the application?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_9_option', 'Aplicación nueva', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Greenfield application'
       and question.question_text = 'Does the application development team understand and actively develop the application?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_12_option', 'Proveedor de soporte externo con proceso de escalado gestionado por tickets; sin recursos propios de soporte', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'External support provider with a ticket-driven escalation process; no inhouse support resources'
       and question.question_text = 'How is the application supported in production?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_13_option', 'Equipo interno separado de soporte, separado del equipo de desarrollo, con poca interacción entre equipos', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Separate internal support team, separate from the development team, with little interaction between the teams'
       and question.question_text = 'How is the application supported in production?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_14_option', 'Multiples equipos proveen soporte usando un modelo establecido de escalado', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Multiple teams provide support using an established escalation model'
       and question.question_text = 'How is the application supported in production?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_15_option', 'Aproximación SRE (Site Reliability Engineering) con equipo de operaciones experimentado y con conocimiento ', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'SRE (Site Reliability Engineering) approach with a knowledgeable and experienced operations team'
       and question.question_text = 'How is the application supported in production?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_16_option', 'Aproximación DevOps con el mismo equipo construyendo la aplicación y soportándola en producción', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'DevOps approach with the same team building the application and supporting it in production'
       and question.question_text = 'How is the application supported in production?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_20_option', 'Más de 6 meses', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'More than 6 months'
       and question.question_text = 'How much time passes from when code is committed until the application is deployed to production?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_21_option', 'De 2 a 6 meses', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = '2-6 months'
       and question.question_text = 'How much time passes from when code is committed until the application is deployed to production?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_22_option', 'De 8 a 30 días', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = '8-30 days'
       and question.question_text = 'How much time passes from when code is committed until the application is deployed to production?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_23_option', 'De 1 a 7 días', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = '1-7 days'
       and question.question_text = 'How much time passes from when code is committed until the application is deployed to production?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_37_option', 'De 1 a 7 días', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = '1-7 days'
       and question.question_text = 'What is the application''s mean time to recover (MTTR) from failure in a production environment?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_24_option', 'Menos de 1 día', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Less than 1 day'
  and question.question_text = 'How much time passes from when code is committed until the application is deployed to production?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_28_option', 'Menos de una vez cada 6 meses', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Less than once every 6 months'
  and question.question_text = 'How often is the application deployed to production?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_29_option', 'Entre una vez al mes y una vez cada 6 meses', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Between once a month and once every 6 months'
  and question.question_text = 'How often is the application deployed to production?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_30_option', 'Semanalmente', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Weekly'
  and question.question_text = 'How often is the application deployed to production?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_31_option', 'Diariamente', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Daily'
  and question.question_text = 'How often is the application deployed to production?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_32_option', 'Varias veces al día', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Several times a day'
  and question.question_text = 'How often is the application deployed to production?'
  and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_36_option', '1 mes o más', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = '1 month or more'
  and question.question_text = 'What is the application''s mean time to recover (MTTR) from failure in a production environment?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_38_option', '1-24 Horas', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = '1-24 hours'
       and question.question_text = 'What is the application''s mean time to recover (MTTR) from failure in a production environment?'
    and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_39_option', 'Menos de 1 hora', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Less than 1 hour'
  and question.question_text = 'What is the application''s mean time to recover (MTTR) from failure in a production environment?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_42_option', 'Múltiples requisitos legales y de licenciamiento', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Multiple legal and licensing requirements'
       and question.question_text = 'Does the application have legal and/or licensing requirements?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_43_option', 'Requisitos de licenciamiento (ejemplos: por servidor, por CPU)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Licensing requirements (examples: per server, per CPU)'
       and question.question_text = 'Does the application have legal and/or licensing requirements?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_44_option', 'Requisitos legales (ejemplos: aislamiento de cluster, hardware, cumplimiento PCI o HIPAA) ', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Legal requirements (examples: cluster isolation, hardware, PCI or HIPAA compliance)'
       and question.question_text = 'Does the application have legal and/or licensing requirements?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_45_option', 'Ninguno', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'None'
       and question.question_text = 'Does the application have legal and/or licensing requirements?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_48_option', 'Monolito masivo (alto uso de memoria y CPU), despliegue único, únicamente escalado vertical', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Massive monolith (high memory and CPU usage), singleton deployment, vertical scale only'
       and question.question_text = 'Which model best describes the application architecture?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_49_option', 'Monolito masivo (alto uso de memoria y CPU), despliegue no único, complejo escalado horizontal', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Massive monolith (high memory and CPU usage), non-singleton deployment, complex to scale horizontally'
       and question.question_text = 'Which model best describes the application architecture?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_50_option', 'Monolito complejo, orden estricto de inicio de dependencias en ejecución, arquitectura no flexible', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Complex monolith, strict runtime dependency startup order, non-resilient architecture'
       and question.question_text = 'Which model best describes the application architecture?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_51_option', 'Monolito flexible (ejemplos: reintentos, Circuit Breaker)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Resilient monolith (examples: retries, circuit breakers)'
       and question.question_text = 'Which model best describes the application architecture?'
       and c.name = 'Application details';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_56_option', 'Requiere una CPU que no está soportada por Red Hat', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Requires CPU that is not supported by Red Hat'
       and question.question_text = 'Does the application require specific hardware?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_57_option', 'Requiere hardware antiguo o personalizado (ejemplo: dispositivo USB)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Requires custom or legacy hardware (example: USB device)'
       and question.question_text = 'Does the application require specific hardware?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_58_option', 'Requiere hardware de computadora específico (ejemplos: GPUs, RAM, HDDs)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Requires specific computer hardware (examples: GPUs, RAM, HDDs)'
       and question.question_text = 'Does the application require specific hardware?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_59_option', 'Requiere una CPU soportada por Red Hat', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Requires CPU that is supported by Red Hat'
       and question.question_text = 'Does the application require specific hardware?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_62_option', 'Sistema operativo que no es compatible con OpenShift Container Platform (ejemplos: OS X, AIX, Unix, Solaris)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Operating system that is not compatible with OpenShift Container Platform (examples: OS X, AIX, Unix, Solaris)'
       and question.question_text = 'What operating system does the application require?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_63_option', 'Linux con drivers personalizados de kernel o una versión específica de kernel', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Linux with custom kernel drivers or a specific kernel version'
       and question.question_text = 'What operating system does the application require?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_64_option', 'Linux con capacidades personalizadas (ejemplos: seccomp, acceso root)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Linux with custom capabilities (examples: seccomp, root access)'
       and question.question_text = 'What operating system does the application require?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_66_option', 'Distribución estándar Linux', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Standard Linux distribution'
       and question.question_text = 'What operating system does the application require?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_69_option', 'No es recomendado la ejecución del componente en un contenedor', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Not recommended to run the component in a container'
       and question.question_text = 'Does the vendor provide support for a third-party component running in a container?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_70_option', 'Sin soporte para contenedores por parte del vendedor', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'No vendor support for containers'
       and question.question_text = 'Does the vendor provide support for a third-party component running in a container?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_71_option', 'El vendedor soporta contenedores pero con limitaciones (ejemplos: funcionalidad restringida, el componente no ha sido verificado)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Vendor supports containers but with limitations (examples: functionality is restricted, component has not been tested)'
       and question.question_text = 'Does the vendor provide support for a third-party component running in a container?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_72_option', 'El vendedor soporta su aplicación en contenedores pero debe construir sus propias imágenes', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Vendor supports their application running in containers but you must build your own images'
       and question.question_text = 'Does the vendor provide support for a third-party component running in a container?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_73_option', 'El vendedor soporta completamente contenedores, provee imágenes certificadas', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Vendor fully supports containers, provides certified images'
       and question.question_text = 'Does the vendor provide support for a third-party component running in a container?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_74_option', 'No hay componentes de terceros requeridos', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'No third-party components required'
       and question.question_text = 'Does the vendor provide support for a third-party component running in a container?'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_77_option', 'Las dependencias son difíciles o caras de cambiar porque son antiguas o de terceros', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Dependencies are difficult or expensive to change because they are legacy or third-party'
       and question.question_text = 'Incoming/northbound dependencies'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_78_option', 'Pueden existir muchas dependencias, se puede cambiar pero el proceso es caro y costoso en tiempo', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Many dependencies exist, can be changed but the process is expensive and time-consuming'
       and question.question_text = 'Incoming/northbound dependencies'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_79_option', 'Pueden existir muchas dependencias, se puede cambiar porque los sistemas son gestionados internamente', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Many dependencies exist, can be changed because the systems are internally managed'
       and question.question_text = 'Incoming/northbound dependencies'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_81_option', 'Sin dependencias entrantes', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'No incoming/northbound dependencies'
       and question.question_text = 'Incoming/northbound dependencies'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_84_option', 'La disponibilidad de la dependencia es verificada solamente cuando la aplicación está procesando tráfico', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Dependency availability only verified when application is processing traffic'
       and question.question_text = 'Outgoing/southbound dependencies'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_85_option', 'Las dependencias requieren un complejo y estricto orden de inicio', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Dependencies require a complex and strict startup order'
       and question.question_text = 'Outgoing/southbound dependencies'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_86_option', 'La aplicación no está lista hasta que las dependencias están verificadas y disponibles', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Application not ready until dependencies are verified available'
       and question.question_text = 'Outgoing/southbound dependencies'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_87_option', 'Proceso limitado disponible si las dependencias no están disponibles', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Limited processing available if dependencies are unavailable'
       and question.question_text = 'Outgoing/southbound dependencies'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_88_option', 'Sin dependencias salientes', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'No outgoing/southbound dependencies'
       and question.question_text = 'Outgoing/southbound dependencies'
       and c.name = 'Application dependencies';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_92_option', 'La aplicación no se puede reiniciar limpiamente después de fallo, requiere intervención manual', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Application cannot be restarted cleanly after failure, requires manual intervention'
       and question.question_text = 'How resilient is the application? How well does it recover from outages and restarts?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_93_option', 'La aplicación falla cuando una dependencia saliente no está disponible y no se recupera automáticamente ', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Application fails when a soutbound dependency is unavailable and does not recover automatically'
       and question.question_text = 'How resilient is the application? How well does it recover from outages and restarts?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_94_option', 'La funcionalidad de la aplicación es limitada cuando una dependencia no está disponible pero se recupera cuando la dependencia está disponible', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Application functionality is limited when a dependency is unavailable but recovers when the dependency is available'
       and question.question_text = 'How resilient is the application? How well does it recover from outages and restarts?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_95_option', 'La aplicación emplea patrones flexibles de arquitectura (ejemplos: Circuit Breaker, mecanismos de reintento)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Application employs resilient architecture patterns (examples: circuit breakers, retry mechanisms)'
       and question.question_text = 'How resilient is the application? How well does it recover from outages and restarts?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_96_option', 'Los contenedores de la aplicación son terminados aleatoriamente para verificar la flexibilidad; se siguen principios de ingeniería del caos', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Application containers are randomly terminated to test resiliency; chaos engineering principles are followed'
       and question.question_text = 'How resilient is the application? How well does it recover from outages and restarts?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_100_option', 'TCP/IP, con nombre de host o dirección IP encapsulada en el mensaje', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'TCP/IP, with host name or IP address encapsulated in the payload'
       and question.question_text = 'How does the external world communicate with the application?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_101_option', 'TCP/UDP sin direccionamiento de host (ejemplo: SSH)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'TCP/UDP without host addressing (example: SSH)'
       and question.question_text = 'How does the external world communicate with the application?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_102_option', 'TCP/UDP encapsulado, usando TLS con cabecera SNI', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'TCP/UDP encapsulated, using TLS with SNI header'
       and question.question_text = 'How does the external world communicate with the application?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_103_option', 'HTTP/HTTPS', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'HTTP/HTTPS'
       and question.question_text = 'How does the external world communicate with the application?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_106_option', 'Los componentes de la aplicación usan memoria compartida en un pod', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Application components use shared memory within a pod'
       and question.question_text = 'How does the application manage its internal state?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_107_option', 'El estado se gestiona externamente por otro producto (ejemplos: Zookeeper o Red Hat Data Grid)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'State is managed externally by another product (examples: Zookeeper or Red Hat Data Grid)'
       and question.question_text = 'How does the application manage its internal state?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_109_option', 'Disco compartido entre instancias de la aplicación', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Disk shared between application instances'
       and question.question_text = 'How does the application manage its internal state?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_110_option', 'Contenedor de almacenaje sin estado o efímero', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Stateless or ephemeral container storage'
       and question.question_text = 'How does the application manage its internal state?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_113_option', 'Usa tecnologías que no son compatibles con Kubernetes (ejemplos: direcciones IP codificadas, gestor personalizado de cluster)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Uses technologies that are not compatible with Kubernetes (examples: hardcoded IP addresses, custom cluster manager)'
       and question.question_text = 'How does the application handle service discovery?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_114_option', 'Requiere un reinicio de aplicación o cluster para descubrir nuevas instancias de servicios', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Requires an application or cluster restart to discover new service instances'
       and question.question_text = 'How does the application handle service discovery?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_115_option', 'Usa tecnologías que son compatibles con Kubernetes pero requiere librerías o servicios específicos (ejemplos: HashiCorp Consul, Netflix Eureka)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Uses technologies that are compatible with Kubernetes but require specific libraries or services (examples: HashiCorp Consul, Netflix Eureka)'
       and question.question_text = 'How does the application handle service discovery?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_116_option', 'Usa resolución de nombres DNS de Kubernetes', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Uses Kubernetes DNS name resolution'
       and question.question_text = 'How does the application handle service discovery?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_120_option', 'Clustering configurado manualmente (ejemplo: clusters estáticos)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Manually configured clustering (example: static clusters)'
       and question.question_text = 'How is the application clustering managed?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_121_option', 'Gestionado por gestor externo de cluster off-PaaS', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Managed by an external off-PaaS cluster manager'
       and question.question_text = 'How is the application clustering managed?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_122_option', 'Gestionado por un entorno de ejecución de la aplicación que es compatible con Kubernetes', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Managed by an application runtime that is compatible with Kubernetes'
       and question.question_text = 'How is the application clustering managed?'
       and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_127_option', 'Los logs no están disponibles o son internos sin forma de ser exportados', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Logs are unavailable or are internal with no way to export them'
       and question.question_text = 'How does the application use logging and how are the logs accessed?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_128_option', 'Los logs están en un formato personalizado binario, expuestos con protocolos no estándar', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Logs are in a custom binary format, exposed with non-standard protocols'
       and question.question_text = 'How does the application use logging and how are the logs accessed?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_129_option', 'Los logs están expuestos usando syslog', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Logs are exposed using syslog'
       and question.question_text = 'How does the application use logging and how are the logs accessed?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_130_option', 'Los logs son escritos a un sistema de ficheros, a veces como múltiples ficheros', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Logs are written to a file system, sometimes as multiple files'
       and question.question_text = 'How does the application use logging and how are the logs accessed?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_131_option', 'Los logs son reenviados a un sistema de log externo (ejemplo: Splunk)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Logs are forwarded to an external logging system (example: Splunk)'
       and question.question_text = 'How does the application use logging and how are the logs accessed?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_132_option', 'Los logs son configurables (ejemplo: pueden ser enviados a stdout)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Logs are configurable (example: can be sent to stdout)'
       and question.question_text = 'How does the application use logging and how are the logs accessed?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_135_option', 'No hay métricas disponibles', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'No metrics available'
       and question.question_text = 'Does the application provide metrics?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_136_option', 'Las métricas son obtenidas pero no expuestas externamente', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Metrics collected but not exposed externally'
       and question.question_text = 'Does the application provide metrics?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_137_option', 'Las métricas expuestas usan protocolos binarios (ejemplos: SNMP, JMX)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Metrics exposed using binary protocols (examples: SNMP, JMX)'
       and question.question_text = 'Does the application provide metrics?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_138_option', 'Las métricas expuestas usan una solución de terceros (ejemplos: Dynatrace, AppDynamics)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Metrics exposed using a third-party solution (examples: Dynatrace, AppDynamics)'
       and question.question_text = 'Does the application provide metrics?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_139_option', 'Las métricas son coleccionadas y expuestas con ayuda de los endpoints incorporados en Prometheus ', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Metrics collected and exposed with built-in Prometheus endpoint support'
       and question.question_text = 'Does the application provide metrics?'
       and c.name = 'Application observability';
INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_143_option', 'Monitorizada y gestionada por un proceso ''watchdog'' personalizado ', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Monitored and managed by a custom watchdog process'
       and question.question_text = 'How easy is it to determine the application''s health and readiness to handle traffic?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_146_option', 'La salud se verifica por exploraciones ejecutando transacciones sintéticas', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Health is verified by probes running synthetic transactions'
  and question.question_text = 'How easy is it to determine the application''s health and readiness to handle traffic?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_149_option', 'Ejecución en tiempo real determinística y previsible o control de requerimientos', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Deterministic and predictable real-time execution or control requirements'
       and question.question_text = 'What best describes the application''s runtime characteristics?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_150_option', 'Sensible a la latencia (ejemplos: aplicaciones de voz, aplicaciones de comercio de alta frecuencia)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Sensitive to latency (examples: voice applications, high frequency trading applications)'
  and question.question_text = 'What best describes the application''s runtime characteristics?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_151_option', 'Tráfico constante con amplio rango de uso de CPU y memoria', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Constant traffic with a broad range of CPU and memory usage'
       and question.question_text = 'What best describes the application''s runtime characteristics?'
    and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_152_option', 'Tráfico intermitente con uso previsible de CPU y memoria', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Intermittent traffic with predictable CPU and memory usage'
  and question.question_text = 'What best describes the application''s runtime characteristics?'
       and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
'SingleOption_153_option', 'Tráfico constante con uso previsible de CPU y memoria', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = 'Constant traffic with predictable CPU and memory usage'
       and question.question_text = 'What best describes the application''s runtime characteristics?'
    and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_156_option', 'Más de 5 minutos', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'More than 5 minutes'
  and question.question_text = 'How long does it take the application to be ready to handle traffic?'
  and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_157_option', '2-5 minutos', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = '2-5 minutes'
  and question.question_text = 'How long does it take the application to be ready to handle traffic?'
  and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_158_option', '1-2 minutos', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = '1-2 minutes'
  and question.question_text = 'How long does it take the application to be ready to handle traffic?'
  and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_159_option', '10-60 segundos', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = '10-60 seconds'
  and question.question_text = 'How long does it take the application to be ready to handle traffic?'
  and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_160_option', 'Menos de 10 segundos', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Less than 10 seconds'
  and question.question_text = 'How long does it take the application to be ready to handle traffic?'
  and c.name = 'Application observability';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_164_option', 'Sin verificación o únicamente verificación manual mínima', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'No testing or minimal manual testing only'
  and question.question_text = 'How is the application tested?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_165_option', 'Verificación automatizada mínima, enfocada en la interfaz de usuario', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Minimal automated testing, focused on the user interface'
  and question.question_text = 'How is the application tested?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_166_option', 'Algunos tests automatizados unitarios y de regresión, verificación básica de la pipeline de CI/CD; no se siguen prácticas de verificación modernas', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Some automated unit and regression testing, basic CI/CD pipeline testing; modern test practices are not followed'
  and question.question_text = 'How is the application tested?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_167_option', 'Verificación automatizada y altamente repetible (ejemplos: unitaria, integración, smoke tests) antes de desplegar a producción; se siguen prácticas modernas de verificación', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Highly repeatable automated testing (examples: unit, integration, smoke tests) before deploying to production; modern test practices are followed'
  and question.question_text = 'How is the application tested?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_168_option', 'Aproximación a la ingeniería del caos, verificación constante en producción (ejemplo: verificación y experimentación A/B)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Chaos engineering approach, constant testing in production (example: A/B testing + experimentation)'
  and question.question_text = 'How is the application tested?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_171_option', 'Los ficheros de configuración compilados durante la instalación y configurados usando una interfaz de usuario', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Configuration files compiled during installation and configured using a user interface'
  and question.question_text = 'How is the application configured?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_172_option', 'Los ficheros de configuración son almacenados externamente (ejemplo: en una base de datos) y accedidos usando claves de entorno específicas (ejemplos: nombre de host, dirección IP)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Configuration files are stored externally (example: in a database) and accessed using specific environment keys (examples: host name, IP address)'
  and question.question_text = 'How is the application configured?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_173_option', 'Multiples ficheros de configuración en múltiples localizaciones de sistema de ficheros', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Multiple configuration files in multiple file system locations'
  and question.question_text = 'How is the application configured?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_174_option', 'Ficheros de configuración construidos dentro de la aplicación y activados usando propiedades de sistema en tiempo de ejecución', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Configuration files built into the application and enabled using system properties at runtime'
  and question.question_text = 'How is the application configured?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_175_option', 'Configuración obtenida de un servidor externo (ejemplos: Spring Cloud Config Server, HashiCorp Consul)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Configuration retrieved from an external server (examples: Spring Cloud Config Server, HashiCorp Consul)'
  and question.question_text = 'How is the application configured?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_176_option', 'Configuración cargada de ficheros en una única localización configurable; se usan variables de entorno', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Configuration loaded from files in a single configurable location; environment variables used'
  and question.question_text = 'How is the application configured?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_179_option', 'Módulos de seguridad de hardware o dispositivos de cifrado', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Hardware security modules or encryption devices'
  and question.question_text = 'How does the application acquire security keys or certificates?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_180_option', 'Claves/certificados ligados a direcciones IP y generados en tiempo de ejecución para cada instancia de aplicación', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Keys/certificates bound to IP addresses and generated at runtime for each application instance'
  and question.question_text = 'How does the application acquire security keys or certificates?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_181_option', 'Claves/certificados compilados dentro de la aplicación', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Keys/certificates compiled into the application'
  and question.question_text = 'How does the application acquire security keys or certificates?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_182_option', 'Cargados de un disco compartido', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Loaded from a shared disk'
  and question.question_text = 'How does the application acquire security keys or certificates?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_183_option', 'Obtenidos de un servidor externo (ejemplos: HashiCorp Vault, CyberArk Conjur)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Retrieved from an external server (examples: HashiCorp Vault, CyberArk Conjur)'
  and question.question_text = 'How does the application acquire security keys or certificates?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_184_option', 'Cargados de ficheros', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Loaded from files'
  and question.question_text = 'How does the application acquire security keys or certificates?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_185_option', 'No requeridos', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Not required'
  and question.question_text = 'How does the application acquire security keys or certificates?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_188_option', 'Despliegue manual usando una interfaz de usuario', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Manual deployment using a user interface'
  and question.question_text = 'How is the application deployed?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_189_option', 'Despliegue manual con alguna automatización', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Manual deployment with some automation'
  and question.question_text = 'How is the application deployed?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_191_option', 'Despliegue automatizado con intervención manual o promoción compleja a través de etapas de pipeline', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Automated deployment with manual intervention or complex promotion through pipeline stages'
  and question.question_text = 'How is the application deployed?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_192_option', 'Despliegue automatizado con una pipeline completa de CI/CD, intervención mínima para la promoción a través de etapas de pipeline', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Automated deployment with a full CI/CD pipeline, minimal intervention for promotion through pipeline stages'
  and question.question_text = 'How is the application deployed?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_193_option', 'Completamente automatizado (GitOps), blue-green, o despliegue ''canary''', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Fully automated (GitOps), blue-green, or canary deployment'
  and question.question_text = 'How is the application deployed?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_197_option', 'Máquina virtual (ejemplos: Red Hat Virtualization, VMware)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Virtual machine (examples: Red Hat Virtualization, VMware)'
  and question.question_text = 'Where is the application deployed?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_198_option', 'Cloud privado (ejemplo: Red Hat OpenStack Platform)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Private cloud (example: Red Hat OpenStack Platform)'
  and question.question_text = 'Where is the application deployed?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_199_option', 'Proveedor público de cloud (ejemplos: Amazon Web Services, Microsoft Azure, Google Cloud Platform)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Public cloud provider (examples: Amazon Web Services, Microsoft Azure, Google Cloud Platform)'
  and question.question_text = 'Where is the application deployed?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_200_option', 'Plataforma como servicio (ejemplos: Heroku, Force.com, Google App Engine)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Platform as a service (examples: Heroku, Force.com, Google App Engine)'
  and question.question_text = 'Where is the application deployed?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_204_option', 'La aplicación se ejecuta en un contenedor en un laptop o computadora de escritorio', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Application runs in a container on a laptop or desktop'
  and question.question_text = 'How mature is the containerization process, if any?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_206_option', 'Alguna experiencia con contenedores pero aún no completamente definida', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Some experience with containers but not yet fully defined'
  and question.question_text = 'How mature is the containerization process, if any?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_207_option', 'Competente con contenedores y plataformas de contenedores (ejemplos: Swarm, Kubernetes)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Proficient with containers and container platforms (examples: Swarm, Kubernetes)'
  and question.question_text = 'How mature is the containerization process, if any?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_208_option', 'La containerización de la aplicación no se ha intentado aún', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Application containerization has not yet been attempted'
  and question.question_text = 'How mature is the containerization process, if any?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_117_option', 'No requiere descubrimiento de servicios', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Does not require service discovery'
  and question.question_text = 'How does the application handle service discovery?'
  and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_123_option', 'Sin gestión de cluster requerida', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'No cluster management required'
  and question.question_text = 'How is the application clustering managed?'
  and c.name = 'Application architecture';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_201_option', 'Otros. Especifique en el campo de comentarios', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Other. Specify in the comments field'
  and question.question_text = 'Where is the application deployed?'
  and c.name = 'Application cross-cutting concerns';

INSERT INTO translated_text (id, deleted, key, text, language)
select nextval('hibernate_sequence'), false,
       'SingleOption_209_option', 'Cloud híbrido (proveedores de cloud públicos y privados)', 'ES'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
  and single_option.option = 'Hybrid cloud (public and private cloud providers)'
  and question.question_text = 'Where is the application deployed?'
  and c.name = 'Application cross-cutting concerns';
