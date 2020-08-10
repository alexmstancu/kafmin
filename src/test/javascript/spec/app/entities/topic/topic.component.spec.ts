/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import TopicComponent from '@/entities/topic/topic.vue';
import TopicClass from '@/entities/topic/topic.component';
import TopicService from '@/entities/topic/topic.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-alert', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Topic Management Component', () => {
    let wrapper: Wrapper<TopicClass>;
    let comp: TopicClass;
    let topicServiceStub: SinonStubbedInstance<TopicService>;

    beforeEach(() => {
      topicServiceStub = sinon.createStubInstance<TopicService>(TopicService);
      topicServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<TopicClass>(TopicComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          topicService: () => topicServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      topicServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllTopics();
      await comp.$nextTick();

      // THEN
      expect(topicServiceStub.retrieve.called).toBeTruthy();
      expect(comp.topics[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      topicServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeTopic();
      await comp.$nextTick();

      // THEN
      expect(topicServiceStub.delete.called).toBeTruthy();
      expect(topicServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
