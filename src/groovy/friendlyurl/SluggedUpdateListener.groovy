package friendlyurl

import org.grails.datastore.mapping.core.Datastore
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEventListener
import org.grails.datastore.mapping.engine.event.EventType
import org.grails.datastore.mapping.model.PersistentEntity
import org.springframework.context.ApplicationEvent

class SluggedUpdateListener extends AbstractPersistenceEventListener {
    SluggedUpdateListener(Datastore datastore) {
        super(datastore)
    }

    @Override
    protected void onPersistenceEvent(AbstractPersistenceEvent event) {
        if (event.eventType in [ EventType.PreInsert, EventType.PreUpdate ]) {
            def subject = event.entityObject
            if (subject.hasProperty('slug')) {
                subject.normalizeSlug()
            }
        }
    }

    @Override
    boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        return true
    }
}
