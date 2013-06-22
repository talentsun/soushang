# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: message.proto

from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)




DESCRIPTOR = _descriptor.FileDescriptor(
  name='message.proto',
  package='',
  serialized_pb='\n\rmessage.proto\" \n\x04User\x12\n\n\x02id\x18\x01 \x02(\x05\x12\x0c\n\x04name\x18\x02 \x02(\t\"\x1b\n\x0bIClientInfo\x12\x0c\n\x04name\x18\x01 \x02(\t\"1\n\nIClientLBS\x12\x11\n\tlongitude\x18\x01 \x02(\x03\x12\x10\n\x08latitude\x18\x02 \x02(\x03\"Z\n\tOQuestion\x12\x11\n\tstatement\x18\x01 \x02(\t\x12\x0f\n\x07options\x18\x02 \x03(\t\x12\r\n\x05right\x18\x03 \x02(\x05\x12\r\n\x05index\x18\x04 \x02(\x05\x12\x0b\n\x03\x61ll\x18\x05 \x02(\x05\"(\n\x07IAnswer\x12\r\n\x05index\x18\x01 \x02(\x05\x12\x0e\n\x06\x63hoose\x18\x02 \x02(\x05\"7\n\x0bOFightState\x12\x0b\n\x03\x61ll\x18\x01 \x02(\x05\x12\x0c\n\x04\x64one\x18\x02 \x02(\x05\x12\r\n\x05right\x18\x03 \x02(\x05\"\x1e\n\x0cOFightResult\x12\x0e\n\x06result\x18\x01 \x02(\x05\"%\n\rOPeerListResp\x12\x14\n\x05users\x18\x01 \x03(\x0b\x32\x05.User\"\x17\n\tIFightReq\x12\n\n\x02id\x18\x01 \x02(\x05\"-\n\nOFightResp\x12\x0e\n\x06result\x18\x01 \x02(\x05\x12\x0f\n\x07message\x18\x02 \x01(\t\" \n\tOFightReq\x12\x13\n\x04user\x18\x01 \x02(\x0b\x32\x05.User\"\x1c\n\nIFightResp\x12\x0e\n\x06result\x18\x01 \x02(\x05')




_USER = _descriptor.Descriptor(
  name='User',
  full_name='User',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='id', full_name='User.id', index=0,
      number=1, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='name', full_name='User.name', index=1,
      number=2, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=17,
  serialized_end=49,
)


_ICLIENTINFO = _descriptor.Descriptor(
  name='IClientInfo',
  full_name='IClientInfo',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='name', full_name='IClientInfo.name', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=51,
  serialized_end=78,
)


_ICLIENTLBS = _descriptor.Descriptor(
  name='IClientLBS',
  full_name='IClientLBS',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='longitude', full_name='IClientLBS.longitude', index=0,
      number=1, type=3, cpp_type=2, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='latitude', full_name='IClientLBS.latitude', index=1,
      number=2, type=3, cpp_type=2, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=80,
  serialized_end=129,
)


_OQUESTION = _descriptor.Descriptor(
  name='OQuestion',
  full_name='OQuestion',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='statement', full_name='OQuestion.statement', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='options', full_name='OQuestion.options', index=1,
      number=2, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='right', full_name='OQuestion.right', index=2,
      number=3, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='index', full_name='OQuestion.index', index=3,
      number=4, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='all', full_name='OQuestion.all', index=4,
      number=5, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=131,
  serialized_end=221,
)


_IANSWER = _descriptor.Descriptor(
  name='IAnswer',
  full_name='IAnswer',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='index', full_name='IAnswer.index', index=0,
      number=1, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='choose', full_name='IAnswer.choose', index=1,
      number=2, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=223,
  serialized_end=263,
)


_OFIGHTSTATE = _descriptor.Descriptor(
  name='OFightState',
  full_name='OFightState',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='all', full_name='OFightState.all', index=0,
      number=1, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='done', full_name='OFightState.done', index=1,
      number=2, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='right', full_name='OFightState.right', index=2,
      number=3, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=265,
  serialized_end=320,
)


_OFIGHTRESULT = _descriptor.Descriptor(
  name='OFightResult',
  full_name='OFightResult',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='result', full_name='OFightResult.result', index=0,
      number=1, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=322,
  serialized_end=352,
)


_OPEERLISTRESP = _descriptor.Descriptor(
  name='OPeerListResp',
  full_name='OPeerListResp',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='users', full_name='OPeerListResp.users', index=0,
      number=1, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=354,
  serialized_end=391,
)


_IFIGHTREQ = _descriptor.Descriptor(
  name='IFightReq',
  full_name='IFightReq',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='id', full_name='IFightReq.id', index=0,
      number=1, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=393,
  serialized_end=416,
)


_OFIGHTRESP = _descriptor.Descriptor(
  name='OFightResp',
  full_name='OFightResp',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='result', full_name='OFightResp.result', index=0,
      number=1, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='message', full_name='OFightResp.message', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=418,
  serialized_end=463,
)


_OFIGHTREQ = _descriptor.Descriptor(
  name='OFightReq',
  full_name='OFightReq',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='user', full_name='OFightReq.user', index=0,
      number=1, type=11, cpp_type=10, label=2,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=465,
  serialized_end=497,
)


_IFIGHTRESP = _descriptor.Descriptor(
  name='IFightResp',
  full_name='IFightResp',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='result', full_name='IFightResp.result', index=0,
      number=1, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=499,
  serialized_end=527,
)

_OPEERLISTRESP.fields_by_name['users'].message_type = _USER
_OFIGHTREQ.fields_by_name['user'].message_type = _USER
DESCRIPTOR.message_types_by_name['User'] = _USER
DESCRIPTOR.message_types_by_name['IClientInfo'] = _ICLIENTINFO
DESCRIPTOR.message_types_by_name['IClientLBS'] = _ICLIENTLBS
DESCRIPTOR.message_types_by_name['OQuestion'] = _OQUESTION
DESCRIPTOR.message_types_by_name['IAnswer'] = _IANSWER
DESCRIPTOR.message_types_by_name['OFightState'] = _OFIGHTSTATE
DESCRIPTOR.message_types_by_name['OFightResult'] = _OFIGHTRESULT
DESCRIPTOR.message_types_by_name['OPeerListResp'] = _OPEERLISTRESP
DESCRIPTOR.message_types_by_name['IFightReq'] = _IFIGHTREQ
DESCRIPTOR.message_types_by_name['OFightResp'] = _OFIGHTRESP
DESCRIPTOR.message_types_by_name['OFightReq'] = _OFIGHTREQ
DESCRIPTOR.message_types_by_name['IFightResp'] = _IFIGHTRESP

class User(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _USER

  # @@protoc_insertion_point(class_scope:User)

class IClientInfo(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _ICLIENTINFO

  # @@protoc_insertion_point(class_scope:IClientInfo)

class IClientLBS(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _ICLIENTLBS

  # @@protoc_insertion_point(class_scope:IClientLBS)

class OQuestion(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _OQUESTION

  # @@protoc_insertion_point(class_scope:OQuestion)

class IAnswer(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _IANSWER

  # @@protoc_insertion_point(class_scope:IAnswer)

class OFightState(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _OFIGHTSTATE

  # @@protoc_insertion_point(class_scope:OFightState)

class OFightResult(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _OFIGHTRESULT

  # @@protoc_insertion_point(class_scope:OFightResult)

class OPeerListResp(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _OPEERLISTRESP

  # @@protoc_insertion_point(class_scope:OPeerListResp)

class IFightReq(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _IFIGHTREQ

  # @@protoc_insertion_point(class_scope:IFightReq)

class OFightResp(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _OFIGHTRESP

  # @@protoc_insertion_point(class_scope:OFightResp)

class OFightReq(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _OFIGHTREQ

  # @@protoc_insertion_point(class_scope:OFightReq)

class IFightResp(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _IFIGHTRESP

  # @@protoc_insertion_point(class_scope:IFightResp)


# @@protoc_insertion_point(module_scope)
