/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import TopicDetailComponent from '@/entities/topic/topic-details.vue';
import TopicClass from '@/entities/topic/topic-details.component';
import TopicService from '@/entities/topic/topic.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Topic Management Detail Component', () => {
    let wrapper: Wrapper<TopicClass>;
    let comp: TopicClass;
    let topicServiceStub: SinonStubbedInstance<TopicService>;

    beforeEach(() => {
      topicServiceStub = sinon.createStubInstance<TopicService>(TopicService);

      wrapper = shallowMount<TopicClass>(TopicDetailComponent, { store, localVue, provide: { topicService: () => topicServiceStub } });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTopic = { id: 123 };
        topicServiceStub.find.resolves(foundTopic);

        // WHEN
        comp.retrieveTopic(123, '');
        await comp.$nextTick();

        // THEN
        expect(comp.topic).toBe(foundTopic);
      });
    });
  });
});
