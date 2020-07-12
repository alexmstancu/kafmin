/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import BrokerComponent from '@/entities/broker/broker.vue';
import BrokerClass from '@/entities/broker/broker.component';
import BrokerService from '@/entities/broker/broker.service';

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
  describe('Broker Management Component', () => {
    let wrapper: Wrapper<BrokerClass>;
    let comp: BrokerClass;
    let brokerServiceStub: SinonStubbedInstance<BrokerService>;

    beforeEach(() => {
      brokerServiceStub = sinon.createStubInstance<BrokerService>(BrokerService);
      brokerServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<BrokerClass>(BrokerComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          brokerService: () => brokerServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      brokerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllBrokers();
      await comp.$nextTick();

      // THEN
      expect(brokerServiceStub.retrieve.called).toBeTruthy();
      expect(comp.brokers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      brokerServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeBroker();
      await comp.$nextTick();

      // THEN
      expect(brokerServiceStub.delete.called).toBeTruthy();
      expect(brokerServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
